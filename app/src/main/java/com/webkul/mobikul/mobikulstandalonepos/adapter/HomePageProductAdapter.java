package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.HomePageProductItemBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.HomeFragmentHandler;

import java.util.List;

/**
 * Created by aman.gupta on 17/1/18. @Webkul Software Private limited
 */

public class HomePageProductAdapter extends RecyclerView.Adapter<HomePageProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;
    private boolean isLowStock;

    public HomePageProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public HomePageProductAdapter(Context context, List<Product> products, boolean isLowStock) {
        this.context = context;
        this.products = products;
        this.isLowStock = isLowStock;
    }

    @Override
    public HomePageProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.home_page_product_item, null, false);
        return new HomePageProductAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(HomePageProductAdapter.ViewHolder holder, int position) {
        holder.binding.setData(products.get(position));
        if (isLowStock)
            holder.binding.lowTag.setVisibility(View.VISIBLE);
        else {
            holder.binding.lowTag.setVisibility(View.GONE);
            holder.binding.setHandler(new HomeFragmentHandler(context));
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final HomePageProductItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = HomePageProductItemBinding.bind(itemView);
        }
    }
}
