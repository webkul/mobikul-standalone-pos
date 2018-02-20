package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.adapter.ManageCategoryAdapter;
import com.webkul.mobikul.mobikulstandalonepos.adapter.ManageOptionsAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentManageOptionsBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ManageOptionFragmentHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import org.jsoup.safety.Whitelist;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class ManageOptionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "product";
    private static final String ARG_PARAM2 = "edit";
    private static String TAG = "ManageOptionsFragment";
    private List<Options> options;
    private Product product;
    private boolean isEdit;
    public FragmentManageOptionsBinding binding;
    public ManageOptionsAdapter manageOptionsAdapter;
    private SweetAlertDialog sweetAlert;

    public ManageOptionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PARAM1);
            isEdit = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_options, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = product.getOptions();
        ((ProductActivity) getContext())
                .setTitle(getContext().getString(R.string.choose_options));
        setProductOptions();
        ((ProductActivity) getActivity()).binding.setData(product);
        ((ProductActivity) getActivity()).binding.setManageOptionFragmentHandler(new ManageOptionFragmentHandler(getActivity()));
        ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.GONE);
        ((ProductActivity) getActivity()).binding.saveProduct.setVisibility(View.GONE);
        ((ProductActivity) getActivity()).binding.saveSelectedOptios.setVisibility(View.GONE);
    }

    public void setProductOptions() {
        DataBaseController.getInstanse().getOptions(getActivity(), new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String msg) {
                if (!responseData.toString().equalsIgnoreCase("[]")) {
                    if (!(options.size() > 0))
                        options.addAll((List<Options>) responseData);
                    manageOptionsAdapter = new ManageOptionsAdapter(getActivity(), options, product);
                    binding.manageOptionsRv.setAdapter(manageOptionsAdapter);
                } else {
                    sweetAlert = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                    sweetAlert.setTitleText(getString(R.string.no_options))
                            .setContentText(getResources().getString(R.string.no_options_subtitle))
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
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                ToastHelper.showToast(getActivity(), errorMsg, Toast.LENGTH_SHORT);
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
        if (isEdit)
            ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.VISIBLE);
        ((ProductActivity) getActivity()).binding.saveProduct.setVisibility(View.VISIBLE);
        ((AddProductFragment) ((ProductActivity) getActivity()).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName())).setProductOptions();
        getActivity().setTitle(getContext().getString(R.string.add_product_title));
    }
}