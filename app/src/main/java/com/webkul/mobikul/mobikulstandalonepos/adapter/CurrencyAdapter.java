package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.CurrencyActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemsCurrencyBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Currency;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CategoryHandler;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CurrencyHandler;

import java.util.List;

/**
 * Created by aman.gupta on 14/3/18. @Webkul Software Private limited
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    private Context context;
    private List<Currency> selectedCurrenciesList;
    private String selectedCurrencyCode;

    public CurrencyAdapter(Context context, List<Currency> selectedCurrencyList) {
        this.context = context;
        this.selectedCurrenciesList = selectedCurrencyList;
    }

    public CurrencyAdapter(Context context, List<Currency> selectedCurrenciesList, String selectedCurrencyCode) {
        this.context = context;
        this.selectedCurrenciesList = selectedCurrenciesList;
        this.selectedCurrencyCode = selectedCurrencyCode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.items_currency, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(selectedCurrenciesList.get(position));
        holder.binding.setHandler(new CurrencyHandler(context));
    }

    @Override
    public int getItemCount() {
        return selectedCurrenciesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemsCurrencyBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
