package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.ProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemCashDrawerBinding;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CashDrawerHandler;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ProductHandler;

import java.util.List;

/**
 * Created by aman.gupta on 16/2/18. @Webkul Software Private limited
 */

class CashDrawerAdapter extends RecyclerView.Adapter<CashDrawerAdapter.ViewHolder> {
    private Context context;
    private List<CashDrawerModel> cashDrawerModelsList;

    public CashDrawerAdapter(Context context, List<CashDrawerModel> cashDrawerModelsList) {
        this.context = context;
        this.cashDrawerModelsList = cashDrawerModelsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_cash_drawer, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(cashDrawerModelsList.get(position));
        holder.binding.setHandler(new CashDrawerHandler(context));
    }

    @Override
    public int getItemCount() {
        return cashDrawerModelsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCashDrawerBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemCashDrawerBinding.bind(itemView);
        }
    }
}
