package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webkul.mobikul.mobikuldialoglibrary.CustomDialog;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.adapter.ManageCategoryAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentManageCategoriesBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ManageCategoriesFragmentHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.FragmentCallBack;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManageCategoriesFragment extends Fragment {
    private static final String ARG_PARAM1 = "product";
    private static final String ARG_PARAM2 = "edit";

    private Product product;
    private String mParam2;
    FragmentManageCategoriesBinding binding;
    private List<Category> categories;
    ManageCategoryAdapter manageCategoryAdapter;
    private SweetAlertDialog sweetAlert;
    private boolean isEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_categories, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categories = new ArrayList<>();
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PARAM1);
            isEdit = getArguments().getBoolean(ARG_PARAM2);
        }
        ((ProductActivity) getContext())
                .setTitle(getContext().getString(R.string.choose_categories));
        DataBaseController.getInstanse().getCategory(getActivity(), new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String msg) {
                if (!responseData.toString().equalsIgnoreCase("[]")) {
                    categories.addAll((List<Category>) responseData);
                    manageCategoryAdapter = new ManageCategoryAdapter(getActivity(), categories, product);
                    binding.manageCategoryRv.setAdapter(manageCategoryAdapter);
                } else {
//                    CustomDialog customDialog = CustomDialog.getInstantDialog(getActivity(), CustomDialog.Type.WARNING_TYPE_DIALOG)
//                            .setButtonEnabled(true)
//                            .setTitleHeading("No categories")
//                            .setSubTitle(R.string.no_category_subtitle);
//                    customDialog.setPositiveButtonClickListener(new CustomDialog.CustomDialogButtonClickListener() {
//                        @Override
//                        public void onClick(CustomDialog customDialog) {
//                            customDialog.dismiss();
//
//
//
//                        }
//                    });
                    sweetAlert = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                    sweetAlert.setTitleText(getString(R.string.no_category))
                            .setContentText(getResources().getString(R.string.no_category_subtitle))
                            .setConfirmText(getResources().getString(R.string.dialog_ok))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    getActivity().onBackPressed();
                                }
                            })
                            .show();
                    sweetAlert.setCancelable(false);
//                    customDialog.setCanceledOnTouchOutside(false);
//                    customDialog.show();
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                ToastHelper.showToast(getActivity(), errorMsg, Toast.LENGTH_SHORT);
            }
        });
        ((ProductActivity) getActivity()).binding.setData(product);
        ((ProductActivity) getActivity()).binding.setManageCategoryFragmentHandler(new ManageCategoriesFragmentHandler(getActivity()));
        ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.GONE);
        ((ProductActivity) getActivity()).binding.saveProduct.setVisibility(View.GONE);
        ((ProductActivity) getActivity()).binding.saveSelectedCategories.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (isEdit)
            ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.VISIBLE);
        ((ProductActivity) getActivity()).binding.saveProduct.setVisibility(View.VISIBLE);
        ((ProductActivity) getActivity()).binding.saveSelectedCategories.setVisibility(View.GONE);
        ((AddProductFragment) ((ProductActivity) getActivity()).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName())).setProductCategory();
        getActivity().setTitle(getContext().getString(R.string.add_product_title));

    }
}
