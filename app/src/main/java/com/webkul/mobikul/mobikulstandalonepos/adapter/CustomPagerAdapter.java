package com.webkul.mobikul.mobikulstandalonepos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobikul.customswipeableviewpager.MobikulAutoScrollableView;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.WalkthroughActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;

public class CustomPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    private int[] mResources;
    private int[] heading;
    private int[] subheading;
    MobikulAutoScrollableView mobikulAutoScrollableView;

    public CustomPagerAdapter(Context context, int[] mResources, int[] heading, int[] subheading) {
        this.heading = heading;
        this.subheading = subheading;
        this.context = context;
        this.mResources = mResources;
        this.heading = heading;
        this.subheading = subheading;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mobikulAutoScrollableView = ((WalkthroughActivity) context).findViewById(R.id.viewpager);
    }

    public CustomPagerAdapter(Context context, int[] mResources, int[] heading, int[] subheading, MobikulAutoScrollableView mobikulAutoScrollableView) {
        this.heading = heading;
        this.subheading = subheading;
        this.context = context;
        this.mResources = mResources;
        this.heading = heading;
        this.subheading = subheading;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mobikulAutoScrollableView = mobikulAutoScrollableView;
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
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.page_item, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);

        ((TextView) itemView.findViewById(R.id.heading)).setText(context.getString(heading[position]));
        ((TextView) itemView.findViewById(R.id.subheading)).setText(context.getString(subheading[position]));
        if (position == mResources.length - 1) {
            itemView.findViewById(R.id.done).setVisibility(View.VISIBLE);
        } else {
            itemView.findViewById(R.id.done).setVisibility(View.GONE);
        }
//
//        itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (Helper.getViewWidth(v) / 2 < event.getX()) {
//                    if (position != mResources.length) {
//                        mobikulAutoScrollableView.getMyCustomViewPager().setCurrentItem(position + 1);
//                        mobikulAutoScrollableView.getMyCustomViewPager().getAdapter().notifyDataSetChanged();
//                        Log.d(TAG, "onTouch: " + "Right" + mobikulAutoScrollableView.getMyCustomViewPager().getCurrentItem());
//                    }
//                } else {
//                    if (position != 0) {
//                        mobikulAutoScrollableView.getMyCustomViewPager().setCurrentItem(position - 1);
//                        mobikulAutoScrollableView.getMyCustomViewPager().getAdapter().notifyDataSetChanged();
//                        Log.d(TAG, "onTouch: " + "Left" + mobikulAutoScrollableView.getMyCustomViewPager().getCurrentItem());
//                    }
//                }
//                return false;
//            }
//        });

        itemView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSharedPref.setShowWalkThrough(context, true);
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