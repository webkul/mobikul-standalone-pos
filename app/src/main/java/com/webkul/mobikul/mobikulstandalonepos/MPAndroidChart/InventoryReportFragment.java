package com.webkul.mobikul.mobikulstandalonepos.MPAndroidChart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.squareup.timessquare.CalendarPickerView;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.SalesAndReportingActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.SalesReportProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentInventoryReportBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.model.SalesProductReportModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class InventoryReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<SalesProductReportModel> soldProducts;
    private SalesReportProductAdapter salesReportProductAdapter;
    HashMap<String, SalesProductReportModel> hashMap;
    private CalendarPickerView calendar;
    static Date from;
    static Date to;
    private FragmentInventoryReportBinding binding;

    public InventoryReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inventory_report, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        from = new Date();
        to = new Date();
        soldProducts = new ArrayList<>();
        hashMap = new HashMap<>();
        binding.productFilter.setIconifiedByDefault(false);
//        binding.productFilter.setIconified(false);
        setSalesProduct();
    }

    private void setSalesProductChart(List<SalesProductReportModel> soldProducts) {
        binding.productChart.setUsePercentValues(true);
        binding.productChart.getDescription().setEnabled(false);
//        binding.productChart.setExtraOffsets(5, 10, 10, 5);
//        holder.chart.setExtraOffsets(5, 10, 50, 10);

        binding.productChart.setDragDecelerationFrictionCoef(0.95f);

        binding.productChart.setCenterText(generateCenterSpannableText());

        binding.productChart.setDrawHoleEnabled(true);
        binding.productChart.setHoleColor(Color.WHITE);

        binding.productChart.setTransparentCircleColor(Color.WHITE);
        binding.productChart.setTransparentCircleAlpha(90);

        binding.productChart.setHoleRadius(50f);
        binding.productChart.setTransparentCircleRadius(55f);

        binding.productChart.setDrawCenterText(true);

        binding.productChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        binding.productChart.setRotationEnabled(true);
        binding.productChart.setHighlightPerTapEnabled(true);

        // binding.productChart.setUnit(" €");
        // binding.productChart.setDrawUnitsInChart(true);

        // add a selection listener
//        binding.productChart.setOnChartValueSelectedListener(this);

        setData(soldProducts, 10);

        binding.productChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // binding.productChart.spin(2000, 0, 360);

        Legend l = binding.productChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        binding.productChart.setEntryLabelColor(Color.WHITE);
        binding.productChart.setEntryLabelTextSize(12f);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Top Selling Products");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
        return s;
    }

    private void setData(List<SalesProductReportModel> soldProducts, float range) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < soldProducts.size(); i++) {
            entries.add(new PieEntry(Float.parseFloat(soldProducts.get(i).getSoldQty()),
                    soldProducts.get(i).getProductName(),
                    getResources().getDrawable(R.drawable.star)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Top Selling Products");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        binding.productChart.setData(data);

        // undo all highlights
        binding.productChart.highlightValues(null);

        binding.productChart.invalidate();
    }

    private void setSalesProduct() {
        DataBaseController.getInstanse().getOrders(getActivity(), new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
                for (OrderEntity orderEntity : (List<OrderEntity>) responseData) {
                    SalesProductReportModel salesProductReportModel;
                    for (Product product : orderEntity.getCartData().getProducts()) {
                        salesProductReportModel = new SalesProductReportModel();
                        salesProductReportModel.setDate(orderEntity.getDate());
                        salesProductReportModel.setPId(product.getPId());
                        salesProductReportModel.setProductName(product.getProductName());
                        salesProductReportModel.setSku(product.getSku());
                        salesProductReportModel.setOptions(product.getOptions());
                        double price;
                        if (product.getSpecialPrice().isEmpty())
                            price = Double.parseDouble(product.getPrice()) * Integer.parseInt(product.getCartQty());
                        else
                            price = Double.parseDouble(product.getSpecialPrice()) * Integer.parseInt(product.getCartQty());
                        for (int i = 0; i < product.getOptions().size(); i++) {
                            if (!product.getOptions().get(i).getType().equalsIgnoreCase("text"))
                                for (OptionValues optionValues : product.getOptions().get(i).getOptionValues()) {
                                    if (optionValues.isAddToCart()) {
                                        if (!optionValues.getOptionValuePrice().isEmpty())
                                            price = price + Integer.parseInt(optionValues.getOptionValuePrice());
                                    }
                                }
                        }

                        price = Helper.currencyConverter(price, getActivity());
                        if (hashMap.containsKey(product.getPId() + "")) {
                            salesProductReportModel.setPrice(Double.parseDouble(hashMap.get(product.getPId() + "").getPrice()) + price + "");
                            salesProductReportModel.setFormattedPrice(Helper.currencyFormater(Double.parseDouble(salesProductReportModel.getPrice()), getActivity()));
                            salesProductReportModel.setSoldQty(Integer.parseInt(hashMap.get(product.getPId() + "").getSoldQty()) + Integer.parseInt(product.getCartQty()) + "");
                        } else {
                            salesProductReportModel.setPrice(price + "");
                            salesProductReportModel.setFormattedPrice(Helper.currencyFormater(price, getActivity()));
                            salesProductReportModel.setSoldQty(product.getCartQty());
                        }
                        hashMap.put(product.getPId() + "", salesProductReportModel);

                    }
                }
                Iterator myVeryOwnIterator = hashMap.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    SalesProductReportModel value = hashMap.get(key);
                    soldProducts.add(value);
                }

                if (salesReportProductAdapter == null) {
                    salesReportProductAdapter = new SalesReportProductAdapter(getActivity(), soldProducts);
                    binding.salesProductRv.setAdapter(salesReportProductAdapter);
                } else {
                    salesReportProductAdapter.notifyDataSetChanged();
                }

                binding.dateFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomDialogClass cdd = new CustomDialogClass(getActivity());
                        cdd.show();
                    }
                });

                binding.productFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        List<SalesProductReportModel> searchProducts = new ArrayList<>();
                        for (SalesProductReportModel tempSalesProduct : soldProducts) {
                            if (tempSalesProduct.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                                searchProducts.add(tempSalesProduct);
                            }
                        }
                        salesReportProductAdapter = new SalesReportProductAdapter(getActivity(), searchProducts);
                        binding.salesProductRv.setAdapter(salesReportProductAdapter);
                        if (newText.isEmpty()) {
                            if (searchProducts.size() > 0)
                                searchProducts.clear();
                            searchProducts.addAll(soldProducts);
                            salesReportProductAdapter.notifyDataSetChanged();
                        }
                        return false;
                    }
                });
                binding.salesProductRv.setNestedScrollingEnabled(false);
                setSalesProductChart(soldProducts);
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    Calendar myCalendar = Calendar.getInstance();

    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        private final SimpleDateFormat sdf;
        public Activity c;
        public Dialog d;
        public TextView yes, no;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            String myFormat = "dd-MMMM-yy"; //In which you need put here
            sdf = new SimpleDateFormat(myFormat, Locale.US);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.filter_by_date);
            yes = (TextView) findViewById(R.id.filter);
            no = (TextView) findViewById(R.id.cancel);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
            findViewById(R.id.from_date).setOnClickListener(this);
            findViewById(R.id.to_date).setOnClickListener(this);
            if (from != null)
                ((TextView) findViewById(R.id.from_date)).setText(sdf.format(from));
            if (to != null)
                ((TextView) findViewById(R.id.to_date)).setText(sdf.format(to));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.filter:
                    filter();
                    break;
                case R.id.cancel:
                    dismiss();
                    break;
                case R.id.from_date:
                    new DatePickerDialog(getActivity(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                case R.id.to_date:
                    new DatePickerDialog(getActivity(), date1, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                default:
                    break;
            }
        }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }
        };

        private void updateLabel() {
            from.setTime(myCalendar.getTime().getTime());
            ((TextView) findViewById(R.id.from_date)).setText(sdf.format(myCalendar.getTime()));
        }

        private void updateLabel1() {
            to.setTime(myCalendar.getTime().getTime());
            ((TextView) findViewById(R.id.to_date)).setText(sdf.format(myCalendar.getTime()));
        }

        private void filter() {
            List<SalesProductReportModel> salesProductReport = new ArrayList<>();
            for (SalesProductReportModel productUnderRange : soldProducts) {
                Date date = new Date(productUnderRange.getDate());
                Log.d(TAG, "filter: " + date);

                if (from.compareTo(date) * date.compareTo(to) > 0) {
                    salesProductReport.add(productUnderRange);
                }
            }
            Log.d(TAG, "filter: " + from);
            Log.d(TAG, "filter: " + to);
            salesReportProductAdapter = new SalesReportProductAdapter(getActivity(), salesProductReport);
            binding.salesProductRv.setAdapter(salesReportProductAdapter);
            dismiss();
        }
    }
}
