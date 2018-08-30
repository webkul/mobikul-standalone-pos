package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.webkul.mobikul.mobikulstandalonepos.MPAndroidChart.DayAxisValueFormatter;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentRevenueReportBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RevenueReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentRevenueReportBinding binding;

    public RevenueReportFragment() {
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_revenue_report, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRevenueChart();
    }

    private void setRevenueChart() {

        DataBaseController.getInstanse().getAllCashHistory(getActivity(), new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
//                binding.revenueChart.setOnChartValueSelectedListener(SalesAndReportingActivity.this);
                binding.revenueChart.setDrawBarShadow(false);
                binding.revenueChart.setDrawValueAboveBar(true);
                binding.revenueChart.getDescription().setEnabled(false);
                // if more than 60 entries are displayed in the chart, no values will be
                // drawn
                binding.revenueChart.setMaxVisibleValueCount(60);
                // scaling can now only be done on x- and y-axis separately
                binding.revenueChart.setPinchZoom(false);

                binding.revenueChart.setDrawGridBackground(false);
                // binding.revenueChart.setDrawYLabels(false);

                List<Integer> xAxisEntry = new ArrayList<>();

                for (CashDrawerModel cashDrawerModel : ((List<CashDrawerModel>) responseData)) {
                    xAxisEntry.add(getCountOfDays(cashDrawerModel.getDate()));
                }
                IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(binding.revenueChart, xAxisEntry);
                XAxis xAxis = binding.revenueChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setLabelCount(7);
                xAxis.setGranularity(1);
                xAxis.setAvoidFirstLastClipping(false);
                xAxis.setValueFormatter(xAxisFormatter);
//                Log.d(TAG, "onSuccess: " + xAxisFormatter.getFormattedValue(77, xAxis));

                Legend l = binding.revenueChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                l.setForm(Legend.LegendForm.CIRCLE);
                l.setFormSize(9f);
                l.setTextSize(11f);
                l.setXEntrySpace(4f);

//                XYMarkerView mv = new XYMarkerView(SalesAndReportingActivity.this, xAxisFormatter);
//                mv.setChartView(binding.revenueChart); // For bounds control
//                binding.revenueChart.setMarker(mv); // Set the marker to the chart

                setData((List<CashDrawerModel>) responseData);
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {

            }
        });


    }

    private void setData(List<CashDrawerModel> cashDrawerData) {

        float start = 1f;
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        for (CashDrawerModel revenueData : cashDrawerData) {
            yVals1.add(new BarEntry(start, Float.parseFloat(revenueData.getNetRevenue())));
            start++;
        }
        BarDataSet set1;

        if (binding.revenueChart.getData() != null &&
                binding.revenueChart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) binding.revenueChart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            binding.revenueChart.getData().notifyDataChanged();
//            binding.revenueChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Revenue");
            set1.setDrawIcons(false);
            set1.setColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            binding.revenueChart.setData(data);
        }
    }


    public int getCountOfDays(String dateString) {
        Date date;
        try {
            SimpleDateFormat format =
                    new SimpleDateFormat("dd-MMMM-yy");
            date = format.parse(dateString);
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
