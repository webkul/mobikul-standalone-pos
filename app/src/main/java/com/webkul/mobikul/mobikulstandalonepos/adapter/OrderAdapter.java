package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemOrderBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.handlers.OrderFragmentHandler;

import java.util.List;

/**
 * Created by aman.gupta on 30/1/18. @Webkul Software Private limited
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<OrderEntity> orders;


    public OrderAdapter(Context context, List<OrderEntity> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_order, null, false);
        return new OrderAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(orders.get(position));
        holder.binding.setHandler(new OrderFragmentHandler(context));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView.getRootView());
        }
    }
}
