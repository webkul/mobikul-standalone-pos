package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemHoldCartBinding;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemOrderBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
import com.webkul.mobikul.mobikulstandalonepos.handlers.HoldCartItemHandler;
import com.webkul.mobikul.mobikulstandalonepos.handlers.OrderFragmentHandler;

import java.util.List;

/**
 * Created by aman.gupta on 6/2/18. @Webkul Software Private limited
 */

public class HoldCartAdapter extends RecyclerView.Adapter<HoldCartAdapter.ViewHolder> {
    private Context context;
    private List<HoldCart> holdCartList;

    public HoldCartAdapter(Context context, List<HoldCart> holdCartList) {
        this.context = context;
        this.holdCartList = holdCartList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_hold_cart, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(holdCartList.get(position));
        holder.binding.setHandler(new HoldCartItemHandler(context));
    }

    @Override
    public int getItemCount() {
        return holdCartList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemHoldCartBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView.getRootView());
        }
    }
}