package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.WalkthroughActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;

public class CustomPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    private int[] mResources;
    private int[] heading;
    private int[] subheading;

    public CustomPagerAdapter(Context context, int[] mResources, int[] heading, int[] subheading) {
        this.heading = heading;
        this.subheading = subheading;
        this.context = context;
        this.mResources = mResources;
        this.heading = heading;
        this.subheading = subheading;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.page_item, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);
        imageView.setImageResource(mResources[position]);

        ((TextView) itemView.findViewById(R.id.heading)).setText(context.getString(heading[position]));
        ((TextView) itemView.findViewById(R.id.subheading)).setText(context.getString(subheading[position]));
        if (position == mResources.length - 1) {
            itemView.findViewById(R.id.done).setVisibility(View.VISIBLE);
        } else {
            itemView.findViewById(R.id.done).setVisibility(View.GONE);
        }

        itemView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSharedPref.setShowWalkThrough(context, false);
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
                ((WalkthroughActivity) context).finish();
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}