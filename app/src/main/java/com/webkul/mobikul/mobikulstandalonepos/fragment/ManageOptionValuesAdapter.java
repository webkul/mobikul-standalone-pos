package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemManageOptionBinding;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemManageOptionValuesBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ManageOptionFragmentHandler;

import java.util.List;

/**
 * Created by aman.gupta on 17/2/18. @Webkul Software Private limited
 */

class ManageOptionValuesAdapter extends RecyclerView.Adapter<ManageOptionValuesAdapter.ViewHolder> {
    private Context context;
    private List<OptionValues> optionValues;

    public ManageOptionValuesAdapter(Context context, List<OptionValues> optionValues) {
        this.context = context;
        this.optionValues = optionValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());
        ItemManageOptionValuesBinding binding = ItemManageOptionValuesBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(optionValues.get(position));
    }

    @Override
    public int getItemCount() {
        return optionValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemManageOptionValuesBinding binding;

        public ViewHolder(ItemManageOptionValuesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OptionValues item) {
            binding.setData(item);
            binding.setHandler(new ManageOptionFragmentHandler(context));
            binding.executePendingBindings();
        }
    }
}
