package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.adapter.ProductCategoryAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentAddProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.AddProductFragmentHandler;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.FragmentCallBack;

import java.io.Serializable;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;

/**
 * Created by aman.gupta on 01/10/17. @Webkul Software Private limited
 */

public class AddProductFragment extends Fragment {
    private static final String ARG_PARAM1 = "product";
    private static final String ARG_PARAM2 = "edit";

    private Product product;
    private String mParam2;
    private boolean isEdit;
    public FragmentAddProductBinding binding;

    public AddProductFragment() {
    }

    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setData(product);
        binding.setHandler(new AddProductFragmentHandler(getActivity()));
        ((ProductActivity) getActivity()).binding.setData(product);
        ((ProductActivity) getActivity()).binding.setAddProductFragmentHandler(new AddProductFragmentHandler(getActivity()));
        if (isEdit) {
            ((ProductActivity) getContext())
                    .setTitle(getContext().getString(R.string.edit_product_title));
            ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.VISIBLE);
        } else {
            ((ProductActivity) getContext())
                    .setTitle(getContext().getString(R.string.add_product_title));
        }
        setProductCategory();
        ((ProductActivity) getActivity()).binding.setEdit(isEdit);
        ((ProductActivity) getActivity()).binding.setData(product);
        ((ProductActivity) getActivity()).binding.addProduct.setVisibility(View.GONE);
        ((ProductActivity) getActivity()).binding.saveProduct.setVisibility(View.VISIBLE);
    }



    public void setProductCategory() {
        if (binding.getData() != null && binding.getData().getProductCategories() != null) {
            ProductCategoryAdapter productCategoryAdapter = new ProductCategoryAdapter(getActivity(), binding.getData().getProductCategories());
            binding.categoryRv.setAdapter(productCategoryAdapter);
            binding.setData(binding.getData());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((ProductActivity) getActivity()).binding.addProduct.setVisibility(View.VISIBLE);
        ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.GONE);
        ((ProductActivity) getActivity()).binding.saveProduct.setVisibility(View.GONE);
        getActivity().recreate();
        ((ProductActivity) getContext())
                .setTitle(getContext().getString(R.string.title_activity_product));
    }

}