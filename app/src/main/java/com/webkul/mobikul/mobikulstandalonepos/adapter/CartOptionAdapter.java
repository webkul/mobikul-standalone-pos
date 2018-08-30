package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ProductOptionsLayoutBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;

import java.util.List;

/**
 * Created by aman.gupta on 20/2/18. @Webkul Software Private limited
 */

class CartOptionAdapter extends RecyclerView.Adapter<CartOptionAdapter.ViewHolder> {
    private Context context;
    private List<Options> options;

    public CartOptionAdapter(Context context, List<Options> options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.product_options_layout, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(options.get(position));
        holder.binding.setPosition(position);
        for (int i = 0; i < options.get(position).getOptionValues().size(); i++) {
            if (options.get(position).getOptionValues().get(i).isAddToCart()) {
                TextView tv = new TextView(context);
                if (!options.get(position).getType().equalsIgnoreCase("text") && !options.get(position).getOptionValues().get(i).getOptionValuePrice().equalsIgnoreCase("")) {
                    tv.setText(options.get(position).getOptionValues().get(i).getOptionValueName() + " (" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(options.get(position).getOptionValues().get(i).getOptionValuePrice()), context), context)+ ")");
                } else
                    tv.setText(options.get(position).getOptionValues().get(i).getOptionValueName());
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                holder.binding.optionValueLl.addView(tv);
            }
        }
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProductOptionsLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
