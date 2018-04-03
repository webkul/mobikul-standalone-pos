package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemProductSalesReportBinding;
import com.webkul.mobikul.mobikulstandalonepos.model.SalesProductReportModel;

import java.util.List;

/**
 * Created by aman.gupta on 16/3/18. @Webkul Software Private limited
 */

public class SalesReportProductAdapter extends RecyclerView.Adapter<SalesReportProductAdapter.ViewHolder> {
    private Context context;
    private List<SalesProductReportModel> soldProducts;

    public SalesReportProductAdapter(Context context, List<SalesProductReportModel> soldProducts) {
        this.context = context;
        this.soldProducts = soldProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_product_sales_report, null, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(soldProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return soldProducts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductSalesReportBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


}
