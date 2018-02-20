package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemManageOptionBinding;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemOptionBinding;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ManageCategoryItemBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageOptionsFragment;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ManageCategoriesFragmentHandler;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ManageOptionFragmentHandler;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by aman.gupta on 17/2/18. @Webkul Software Private limited
 */

public class ManageOptionsAdapter extends RecyclerView.Adapter<ManageOptionsAdapter.ViewHolder> {
    private static String TAG = "ManageOptionsAdapter";
    private Context context;
    private List<Options> options;
    private Product product;

    public ManageOptionsAdapter(Context context, List<Options> options, Product product) {
        this.context = context;
        this.options = options;
        this.product = product;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());
        ItemManageOptionBinding binding = ItemManageOptionBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        if (product.getOptions().size() > 0 && product.getOptions().get(position).getOptionId() == options.get(position).getOptionId()) {
//            if (product.getOptions().get(position).isSelected())
//                options.get(position).setSelected(true);
//        }
        holder.bind(options.get(position));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemManageOptionBinding binding;

        public ViewHolder(ItemManageOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Options item) {
            binding.setData(item);
            binding.setProduct(product);
            binding.setHandler(new ManageOptionFragmentHandler(context));
            binding.executePendingBindings();
        }
    }
}
