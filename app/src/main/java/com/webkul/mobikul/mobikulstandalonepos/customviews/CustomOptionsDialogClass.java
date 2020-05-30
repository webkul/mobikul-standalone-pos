package com.webkul.mobikul.mobikulstandalonepos.customviews;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class CustomOptionsDialogClass extends DialogFragment implements
        android.view.View.OnClickListener {

    public Button mYesButton, mNoButton;
    private Context mContext;
    private Product mProduct;
    private CartModel mCartData;
    private int mClickedUploadButtonId;
    private CustomOptionsDialogClassInteraction mCustomOptionsDialogClassInteraction;
    private ArrayList<OptionValues> mOptionValuesListForFleOption;
    //Constants
    private String DAY_MONTH_YEAR = "dd/MM/yyyy";
    private final int SELECT_FILE = 3;

    /*Method to set product and cart data
     * @calling - from HomeFragmentHandler class
     * @click - on product click*/
    public void setData(Product product, CartModel cartData) {
        this.mProduct = product;
        this.mCartData = cartData;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mCustomOptionsDialogClassInteraction = (CustomOptionsDialogClassInteraction) context;
    }

    /*Method to set dialog attribute*/
    private void setDialogWindowAttribute() {
        Log.d(TAG, " setDialogWindowAttribute()  no title and outside touch cancel false");
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_options, container, false);
        setDialogWindowAttribute();
        createOptionView(view);
        return view;
    }

    /*Method to set option and option value list in dialog view
     * @click - on Product click on HomeFragment
     * @View- Dialog fragment view*/
    private void createOptionView(View view) {
        for (final Options options : mProduct.getOptions()) {
            if (options.isSelected()) {
                TextView label = new TextView(mContext);
                label.setText(options.getOptionName());
                ((LinearLayout) view.findViewById(R.id.options)).addView(label);
                Log.d("Option - ", options.isSelected() + "");
                switch (options.getType()) {
                    case "Select":
                    case "Radio":
                        RadioGroup rg = new RadioGroup(mContext);
                        for (OptionValues optionValues : options.getOptionValues()) {
                            if (optionValues.isSelected()) {
                                RadioButton optionValuesRadio = new RadioButton(mContext);

                                if (!optionValues.getOptionValuePrice().isEmpty())
                                    optionValuesRadio.setText(optionValues.getOptionValueName() + "(" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), mContext), mContext) + ")");
                                else
                                    optionValuesRadio.setText(optionValues.getOptionValueName());
                                optionValuesRadio.setTag(optionValues);
                                rg.addView(optionValuesRadio);
                                if (optionValues.isAddToCart()) {
                                    optionValuesRadio.setChecked(true);
                                }
                            }
                        }
                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                for (OptionValues optionValues : options.getOptionValues()) {
                                    optionValues.setAddToCart(false);
                                }
                                OptionValues optionValues = (OptionValues) getDialog().findViewById(checkedId).getTag();
                                if (((RadioButton) getDialog().findViewById(checkedId)).isChecked())
                                    optionValues.setAddToCart(true);
                            }
                        });
                        ((LinearLayout) view.findViewById(R.id.options)).addView(rg);
                        break;
                    case "Checkbox":
                        for (OptionValues optionValues : options.getOptionValues()) {
                            if (optionValues.isSelected()) {

                                CheckBox optionValuesCheckBox = new CheckBox(mContext);
                                if (optionValues.isAddToCart()) {
                                    optionValuesCheckBox.setChecked(true);
                                }
                                if (!optionValues.getOptionValuePrice().isEmpty())
                                    optionValuesCheckBox.setText(optionValues.getOptionValueName() + "(" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), mContext), mContext) + ")");
                                else
                                    optionValuesCheckBox.setText(optionValues.getOptionValueName());
                                optionValuesCheckBox.setTag(optionValues);
                                optionValuesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        OptionValues optionValues = (OptionValues) buttonView.getTag();
                                        if (isChecked)
                                            optionValues.setAddToCart(true);
                                        else
                                            optionValues.setAddToCart(false);
                                    }


                                });
                                ((LinearLayout) view.findViewById(R.id.options)).addView(optionValuesCheckBox);
                            }
                        }
                        break;
                    case "Text":
                    case "TextArea":
                        EditText text = new EditText(mContext);
                        text.setLayoutParams(new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT));
                        text.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                OptionValues optionValues = new OptionValues();
                                optionValues.setAddToCart(true);
                                optionValues.setOptionValueName(s + "");
                                optionValues.setSelected(true);
                                List<OptionValues> optionValuesList = new ArrayList<>();
                                optionValuesList.add(optionValues);
                                options.setOptionValues(optionValuesList);
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                        ((LinearLayout) view.findViewById(R.id.options)).addView(text);
                        break;
                    case "File":
                        handleForFile(options, view);
                        break;
                    case "Date":
                        handleForDate(options, view);
                        break;
                    case "Time":
                        handleForTime(options, view);
                        break;
                    case "Date & Time":
                        handleForTimeAndDate(options, view);
                        break;


                }
            }
        }
        mYesButton = view.findViewById(R.id.btn_yes);
        mNoButton = view.findViewById(R.id.btn_no);
        mYesButton.setOnClickListener(this);
        mNoButton.setOnClickListener(this);
    }

    /**
     * Method to create option for Time Type option and will handle the further processing for Time Type Option
     *
     * @param options- option
     * @param view     - Dialog fragment view to add other option view of Time type
     */
    private void handleForTime(Options options, View view) {
        for (final OptionValues optionValues : options.getOptionValues()) {
            if (optionValues.isSelected()) {
                TextView optionLabel = new TextView(mContext);
                EditText timeEditText = new EditText(mContext);
                timeEditText.setInputType(InputType.TYPE_NULL);
                timeEditText.setId(View.generateViewId());
                // viewIdCount++;
                optionLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                timeEditText.setLayoutParams(new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!optionValues.getOptionValuePrice().isEmpty()) {
                    String Text = optionValues.getOptionValueName() + "(" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), mContext), mContext) + ")";
                    optionLabel.setText(Text);
                } else
                    optionLabel.setText(optionValues.getOptionValueName());
                timeEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);
                        new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                EditText editText = (EditText) getDialog().findViewById(view.getId());
                                editText.setFocusable(false);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    editText.setShowSoftInputOnFocus(false);
                                }

                                editText.setText(getFormattedTime(i, i1));
                                optionValues.setAddToCart(true);
                                optionValues.setOptionValueName(optionValues.getOptionValueName().concat("\n" + editText.getText().toString()));

                            }
                        }, hour, minute, false).show();
                    }
                });
                ((LinearLayout) view.findViewById(R.id.options)).addView(optionLabel);
                ((LinearLayout) view.findViewById(R.id.options)).addView(timeEditText);
            }
        }

    }

    /**
     * Method to create option for File Type option and will handle the further processing for File Type Option
     *
     * @param options- option
     * @param view     - Dialog fragment view to add other option view of Time type
     */
    private void handleForFile(Options options, View view) {
        for (final OptionValues optionValues : options.getOptionValues()) {
            if (optionValues.isSelected()) {
                if (mOptionValuesListForFleOption == null)
                    mOptionValuesListForFleOption = new ArrayList<>();
                mOptionValuesListForFleOption.add(optionValues);
                TextView optionLabel = new TextView(mContext);
                Button uploadButton = new Button(mContext);
                uploadButton.setText(getResources().getString(R.string.upload));
                uploadButton.setId(View.generateViewId());
                optionLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                uploadButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!optionValues.getOptionValuePrice().isEmpty()) {
                    String text = optionValues.getOptionValueName() + "(" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), mContext), mContext) + ")";
                    optionLabel.setText(text);
                } else
                    optionLabel.setText(optionValues.getOptionValueName());
                uploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        mClickedUploadButtonId = view.getId();
                        optionValues.setOptionId(mClickedUploadButtonId);
                        if (getDialog().findViewById(view.getId()) != null && getDialog().findViewById(view.getId()) instanceof Button) {
                            Button button = getDialog().findViewById(view.getId());
                            if (button.getText().toString().equalsIgnoreCase(getResources().getString(R.string.upload))) {
                                //optionValues.setAddToCart(true);
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                if (getActivity() != null)
                                    getActivity().startActivityForResult(intent, SELECT_FILE);
                            } else {
                                button.setText(getResources().getString(R.string.upload));

                                optionValues.setAddToCart(false);
                                int cutIndex = optionValues.getOptionValueName().lastIndexOf('/');
                                if (cutIndex != -1) {
                                    String valueName = optionValues.getOptionValueName().substring(0, cutIndex);
                                    optionValues.setOptionValueName(valueName);
                                }
                                Toast.makeText(mContext, " File Removed successfuly", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
                ((LinearLayout) view.findViewById(R.id.options)).addView(optionLabel);
                ((LinearLayout) view.findViewById(R.id.options)).addView(uploadButton);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && getDialog().findViewById(mClickedUploadButtonId) instanceof Button) {
            Button button = null;
            if (getDialog().findViewById(mClickedUploadButtonId) instanceof Button) {
                button = getDialog().findViewById(mClickedUploadButtonId);
            }
            String fileName = "";
            if (data != null) {
                fileName = getFileName(data.getData());
               /* Uri uri = data.getData();
                if (uri != null) {
                    String filePath = uri.getPath();
                    int cutIndex = filePath.lastIndexOf('/');
                    if (cutIndex != -1) {
                        fileName = filePath.substring(cutIndex + 1);
                    }
                }*/
            }
            // to add option value to cart and add file name to optionVale
            for (int i = 0; i < mOptionValuesListForFleOption.size(); i++) {
                OptionValues optionValues = mOptionValuesListForFleOption.get(i);
                if (optionValues.getOptionId() == mClickedUploadButtonId) {
                    optionValues.setAddToCart(true);
                    optionValues.setOptionValueName(optionValues.getOptionValueName().concat("\n" + fileName));
                    if (button != null)
                        button.setText(getResources().getString(R.string.remove));
                    Toast.makeText(mContext, fileName + " File Uploaded successfuly", Toast.LENGTH_LONG).show();
                }

            }

        }


    }

    /**
     * Method to get file name from uri coming after selecting file
     *
     * @param uri - uri
     * @return - will return file name
     */
    private String getFileName(Uri uri) {
        String fileName = "";
        if (uri != null) {
            String filePath = uri.getPath();
            int cutIndex = filePath.lastIndexOf('/');
            if (cutIndex != -1) {
                fileName = filePath.substring(cutIndex + 1);
            }
        }
        return fileName;
    }

    @Override
    public void onResume() {
        super.onResume();
        setDialogHeightAndWidth();
    }

    /**
     * Method to set Dialog fragment Height and Width
     */
    private void setDialogHeightAndWidth() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            assert window != null;
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    /**
     * Method to create option for Date Type option and will handle the further processing for Date Type Option
     *
     * @param options- option
     * @param view     - Dialog fragment view to add other option view of Time type
     */
    private void handleForDate(Options options, View view) {
        for (final OptionValues optionValues : options.getOptionValues()) {
            if (optionValues.isSelected()) {
                TextView optionLabel = new TextView(mContext);
                EditText dateEditText = new EditText(mContext);
                dateEditText.setInputType(InputType.TYPE_NULL);
                dateEditText.setId(View.generateViewId());
                // viewId++;
                optionLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                dateEditText.setLayoutParams(new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!optionValues.getOptionValuePrice().isEmpty()) {
                    String text = optionValues.getOptionValueName() + "(" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), mContext), mContext) + ")";
                    optionLabel.setText(text);
                } else
                    optionLabel.setText(optionValues.getOptionValueName());
                dateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                String finalDate = getFormattedDate(dayOfMonth, month + 1, year, DAY_MONTH_YEAR, null);
                                EditText editText = (EditText) getDialog().findViewById(view.getId());
                                if (editText != null) {
                                    editText.setFocusable(false);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        editText.setShowSoftInputOnFocus(false);
                                    }
                                    editText.setText(finalDate);
                                    optionValues.setAddToCart(true);
                                    optionValues.setOptionValueName(optionValues.getOptionValueName().concat("\n" + editText.getText().toString()));
                                }

                            }
                        }, year, month, day).

                                show();
                    }
                });
                ((LinearLayout) view.findViewById(R.id.options)).addView(optionLabel);
                ((LinearLayout) view.findViewById(R.id.options)).addView(dateEditText);
            }
        }
    }

    /**
     * Method to create option for TimeAndDate Type option and will handle the further processing for TimeAndDate Type Option
     *
     * @param options- option
     * @param view     - Dialog fragment view to add other option view of Time type
     */
    private void handleForTimeAndDate(Options options, View view) {
        for (final OptionValues optionValues : options.getOptionValues()) {
            if (optionValues.isSelected()) {
                TextView optionLabel = new TextView(mContext);
                EditText dateEditText = new EditText(mContext);
                dateEditText.setInputType(InputType.TYPE_NULL);
                dateEditText.setId(View.generateViewId());
                //viewId++;
                optionLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                dateEditText.setLayoutParams(new LinearLayout.LayoutParams(600, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!optionValues.getOptionValuePrice().isEmpty()) {
                    String text = optionValues.getOptionValueName() + "(" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), mContext), mContext) + ")";
                    optionLabel.setText(text);
                } else
                    optionLabel.setText(optionValues.getOptionValueName());
                dateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                final String finalDate = getFormattedDate(dayOfMonth, month + 1, year, DAY_MONTH_YEAR, null);
                                final Calendar c = Calendar.getInstance();
                                int hour = c.get(Calendar.HOUR_OF_DAY);
                                int minute = c.get(Calendar.MINUTE);

                                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        EditText editText = (EditText) getDialog().findViewById(view.getId());
                                        String finalText = finalDate + " " + getFormattedTime(i, i1);
                                        editText.setText(finalText);
                                        optionValues.setAddToCart(true);
                                        optionValues.setOptionValueName(optionValues.getOptionValueName().concat("\n" + editText.getText().toString()));
                                    }
                                }, hour, minute, false).show();

                            }
                        }, year, month, day).show();
                    }
                });
                ((LinearLayout) view.findViewById(R.id.options)).addView(optionLabel);
                ((LinearLayout) view.findViewById(R.id.options)).addView(dateEditText);
            }
        }
    }

    /**
     * Method To format time after selecting
     *
     * @param hourOfDay - hour value
     * @param minute    - minute value
     * @return - formatted time in String
     */
    private String getFormattedTime(int hourOfDay, int minute) {
        String minute_precede = "", hour_precede = ""/*, finalTIme = ""*/, amOrPm = "";
        int hourValue = 0;

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM) {
            amOrPm = "AM";
            if (hourOfDay == 0)
                hourValue = 12;
            else
                hourValue = hourOfDay;
        } else if (datetime.get(Calendar.AM_PM) == Calendar.PM) {
            amOrPm = "PM";
            if (hourOfDay == 12)
                hourValue = 12;
            else if (hourOfDay >= 13 && hourOfDay <= 23)
                hourValue = hourOfDay - 12;
        }
        if (hourValue < 10)
            hour_precede = "0";// to add zero before hour when time hour is in single digit
        if (minute < 10)
            minute_precede = "0";// to add zero before minute when minute value in single digit
        return (hour_precede + hourValue + ":" + minute_precede + minute + " " + amOrPm);
    }

    /*Method to return formatted Date*/
    private String getFormattedDate(int day, int month, int year, String format, String date) {
        Log.d(TAG, "getFormattedDate(): day " + day + " month " + month + " year " + year + "date " + date + " format " + format);
        if (date == null)
            date = year + "-" + month + "-" + day;
        if (date.contains("T"))
            date = date.split("T")[0];
        String YEAR_MONTH_DAY = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY, Locale.getDefault());
        try {
            return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(sdf.parse(date).getTime()));
        } catch (Exception e) {
            Log.e(TAG, "getFormattedDate(): e " + e);
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                if (isOptionsValidate(mProduct)) {
                    if (mCartData == null)
                        mCartData = Helper.fromStringToCartModel(AppSharedPref.getCartData(mContext));
                    mCustomOptionsDialogClassInteraction.addProductToCart(mProduct, mCartData);
                    getDialog().findViewById(R.id.error_text).setVisibility(View.GONE);
                    dismiss();
                } else {
                    Helper.shake(mContext, getDialog().findViewById(R.id.dialog_ll));
                    getDialog().findViewById(R.id.error_text).setVisibility(View.VISIBLE);
                }
                break;
            default:
                dismiss();
                break;
        }
    }


    boolean isOptionsValidate(Product product) {
        int count = 0;
        int enabledOptionCount = 0;
        for (int i = 0; i < product.getOptions().size(); i++) {
            if (product.getOptions().get(i).isSelected())
                for (OptionValues optionValues : product.getOptions().get(i).getOptionValues()) {
                    if (optionValues.isAddToCart()) {
                        count++;
                        break;
                    }
                }
        }

        for (Options options : product.getOptions()) {
            if (options.isSelected())
                enabledOptionCount++;
        }

        if (count == enabledOptionCount)
            return true;
        else
            return false;
    }

    /**
     * interface to interact with main activity to add product to cart
     */
    public interface CustomOptionsDialogClassInteraction {
        void addProductToCart(Product product, CartModel cartData);

    }
}
