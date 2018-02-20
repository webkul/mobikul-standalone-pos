package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemCartProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by aman.gupta on 18/1/18. @Webkul Software Private limited
 */

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;

    public CartProductAdapter(Context context, List<Product> products) {

        this.context = context;
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_cart_product, null, false);

        return new CartProductAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(products.get(position));
        List<Options> optionList = new ArrayList<>();
        for (Options options : products.get(position).getOptions()) {
            if (options.isSelected()) {
                optionList.add(options);
            }
        }

        CartOptionAdapter cartOptionAdapter = new CartOptionAdapter(context, optionList);
        holder.binding.optionRv.setAdapter(cartOptionAdapter);
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
}
