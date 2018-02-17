package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.OptionsActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemOptionBinding;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.handlers.OptionHandler;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ProductHandler;

import java.util.List;

/**
 * Created by aman.gupta on 17/2/18. @Webkul Software Private limited
 */

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {
    private Context context;
    private List<Options> options;

    public OptionsAdapter(Context context, List<Options> options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_option, null, false);
        return new OptionsAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(options.get(position));
        holder.binding.setHandler(new OptionHandler(context));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOptionBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemOptionBinding.bind(itemView);
        }
    }
}
