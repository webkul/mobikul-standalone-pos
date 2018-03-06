package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemOptionValueBinding;
import com.webkul.mobikul.mobikulstandalonepos.handlers.OptionHandler;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;

import java.util.List;

/**
 * Created by aman.gupta on 16/2/18. @Webkul Software Private limited
 */

public class OptionValuesAdapter extends RecyclerView.Adapter<OptionValuesAdapter.ViewHolder> {
    private Context context;
    private List<OptionValues> optionValues;

    public OptionValuesAdapter(Context context, List<OptionValues> optionValues) {
        this.context = context;
        this.optionValues = optionValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_option_value, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(optionValues.get(position));
        holder.binding.setHandler(new OptionHandler(context));
    }

    @Override
    public int getItemCount() {
        return optionValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemOptionValueBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
