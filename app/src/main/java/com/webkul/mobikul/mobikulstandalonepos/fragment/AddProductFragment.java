package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.content.ContentValues;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.adapter.ProductCategoryAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentAddProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.AddProductFragmentHandler;

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
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PARAM1);
            isEdit = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setData(product);
        binding.setHandler(new AddProductFragmentHandler(getActivity()));
        if (getActivity() != null) {
            ((ProductActivity) getActivity()).binding.setData(product);
        }
        ((ProductActivity) getActivity()).binding.setAddProductFragmentHandler(new AddProductFragmentHandler(getActivity()));
        if (isEdit) {
            if (getContext() != null) {
                ((ProductActivity) getContext())
                        .setTitle(getContext().getString(R.string.edit_product_title));
            }
            ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.VISIBLE);
            setBarcode(product.getBarCode());
        } else {
            if (getContext() != null) {
                ((ProductActivity) getContext())
                        .setTitle(getContext().getString(R.string.add_product_title));
            }
            ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.GONE);
        }
        setProductCategory();
        binding.setEdit(isEdit);
        ((ProductActivity) getActivity()).binding.setEdit(isEdit);
        ((ProductActivity) getActivity()).binding.setData(product);
        ((ProductActivity) getActivity()).binding.addProduct.setVisibility(View.GONE);
        ((ProductActivity) getActivity()).binding.saveProduct.setVisibility(View.VISIBLE);
    }

    public void setBarcode(String barCodeNumber) {
        String text = barCodeNumber + ""; // Whatever you need to encode in the QR code
        Log.d(ContentValues.TAG, "generateBarcode: " + text);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODABAR, 380, 100);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            binding.barCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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
        if (getActivity() != null) {
            ((ProductActivity) getActivity()).binding.addProduct.setVisibility(View.VISIBLE);
            ((ProductActivity) getActivity()).binding.deleteProduct.setVisibility(View.GONE);
            ((ProductActivity) getActivity()).binding.saveProduct.setVisibility(View.GONE);
            getActivity().recreate();
            if (getContext() != null) {
                ((ProductActivity) getContext())
                        .setTitle(getContext().getString(R.string.title_activity_product));
            }
        }
    }
}