package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.MoreRvItemsBinding;
import com.webkul.mobikul.mobikulstandalonepos.handlers.MoreFragmentHandler;
import com.webkul.mobikul.mobikulstandalonepos.model.MoreData;

import java.util.List;

/**
 * Created by aman.gupta on 4/1/18.
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {
    private Context context;
    private List<MoreData> data;

    public MoreAdapter(Context context, List<MoreData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.more_rv_items, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(data.get(position));
        holder.binding.setHandler(new MoreFragmentHandler(context));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MoreRvItemsBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = MoreRvItemsBinding.bind(itemView);
        }
    }
}
