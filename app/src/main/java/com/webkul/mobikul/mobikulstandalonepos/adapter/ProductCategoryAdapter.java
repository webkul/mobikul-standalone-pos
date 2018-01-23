package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.databinding.ManageCategoryItemBinding;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ProductCategoriesBinding;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.util.List;

/**
 * Created by aman.gupta on 12/1/18. @Webkul Software Private limited
 */

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder> {
    private Context context;
    private List<ProductCategoryModel> productCategories;

    public ProductCategoryAdapter(Context context, List<ProductCategoryModel> productCategories) {
        this.context = context;
        this.productCategories = productCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());
        ProductCategoriesBinding binding = ProductCategoriesBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(productCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return productCategories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProductCategoriesBinding binding;

        public ViewHolder(ProductCategoriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
