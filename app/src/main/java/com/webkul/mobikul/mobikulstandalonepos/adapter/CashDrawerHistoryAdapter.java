package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemCashDrawerBinding;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemCashDrawerHistoryBinding;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CashDrawerHandler;
import com.webkul.mobikul.mobikulstandalonepos.model.CashDrawerItems;

import java.util.List;

/**
 * Created by aman.gupta on 16/2/18. @Webkul Software Private limited
 */

public class CashDrawerHistoryAdapter extends RecyclerView.Adapter<CashDrawerHistoryAdapter.ViewHolder> {
    private Context context;
    private List<CashDrawerItems> cashDrawerItems;

    public CashDrawerHistoryAdapter(Context context, List<CashDrawerItems> cashDrawerItems) {
        this.context = context;
        this.cashDrawerItems = cashDrawerItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_cash_drawer_history, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(cashDrawerItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cashDrawerItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCashDrawerHistoryBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemCashDrawerHistoryBinding.bind(itemView);

        }
    }
}
