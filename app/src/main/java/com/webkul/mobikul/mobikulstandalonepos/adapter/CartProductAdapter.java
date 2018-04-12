package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.CartActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemCartProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CartHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by aman.gupta on 18/1/18. @Webkul Software Private limited
 */

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private Context context;
    public List<Product> products;
    private boolean fromCart = false;
    private CartModel cartData;
    private SparseBooleanArray mSelectedItemsIds;

    public CartProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public CartProductAdapter(Context context, List<Product> products, boolean fromCart) {
        this.context = context;
        this.products = products;
        this.fromCart = fromCart;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public CartProductAdapter(Context context, List<Product> products, boolean fromCart, CartModel cartData) {
        this.context = context;
        this.products = products;
        this.fromCart = fromCart;
        this.cartData = cartData;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_cart_product, null, false);
        return new CartProductAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (products.get(position).getFormattedDiscount().isEmpty())
            products.get(position).setFormattedDiscount(Helper.currencyFormater(0, context));
        holder.binding.setData(products.get(position));
        holder.binding.setCartData(cartData);
        List<Options> optionList = new ArrayList<>();
        for (Options options : products.get(position).getOptions()) {
            if (options.isSelected()) {
                optionList.add(options);
            }
        }
        if (mSelectedItemsIds.get(position))
            holder.binding.cartProductItemRl.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundColor));
        else
            holder.binding.cartProductItemRl.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        CartOptionAdapter cartOptionAdapter = new CartOptionAdapter(context, optionList);
        holder.binding.optionRv.setAdapter(cartOptionAdapter);
        holder.binding.setHandler(new CartHandler(context));
        if (fromCart) {
            holder.binding.plus.setVisibility(View.VISIBLE);
            holder.binding.minus.setVisibility(View.VISIBLE);
            holder.binding.discountLl.setEnabled(true);
        } else {
            holder.binding.plus.setVisibility(View.GONE);
            holder.binding.minus.setVisibility(View.GONE);
            holder.binding.discountLl.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartProductBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    //    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    //
//    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    //
//    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    //
//    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //
//    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
