package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemOptionBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;

import java.util.List;

/**
 * Created by aman.gupta on 19/2/18. @Webkul Software Private limited
 */

public class ProductOptionsAdapter extends RecyclerView.Adapter<ProductOptionsAdapter.ViewHolder> {

    private Context context;
    private List<Options> options;

    public ProductOptionsAdapter(Context context, List<Options> options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());
        ItemOptionBinding binding = ItemOptionBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (options.get(position).isSelected())
            holder.binding.setData(options.get(position));
        holder.binding.arrow.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemOptionBinding binding;

        public ViewHolder(ItemOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
