package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobikul.customswipeableviewpager.MobikulAutoScrollableView;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.CustomPagerAdapter;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;

public class WalkthroughActivity extends AppCompatActivity {

    private MobikulAutoScrollableView mobikulAutoScrollableView;
    private CustomPagerAdapter mCustomPagerAdapter;
    int res[] = {R.drawable.wkt_screen_1, R.drawable.wkt_screen_2, R.drawable.wkt_screen_3, R.drawable.wkt_screen_4, R.drawable.wkt_screen_5};
    int heading[] = {R.string.welcome_to_webkul_stand_alone_pos, R.string.easily_manage_the_cart, R.string.many_options, R.string.manage_products, R.string.category_management};
    int[] subheading = {R.string.user_can_add_product_to_cart_by_simply_tab_and_by_search, R.string.subheading_cart_wk_msg, R.string.manage_customer_product_categoies_taxes_and_a_lot_more, R.string.simply_manage_the_all_types_of_products, R.string.you_can_simply_add_and_manage_the_categoies};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mobikulAutoScrollableView = findViewById(R.id.viewpager);
        mCustomPagerAdapter = new CustomPagerAdapter(this, res, heading, subheading, mobikulAutoScrollableView);
        mobikulAutoScrollableView.showBullets();
        mobikulAutoScrollableView.setAdapter(mCustomPagerAdapter);
    }

    public void clickLeft(View v) {
        if (mobikulAutoScrollableView.getMyCustomViewPager().getCurrentItem() != 0) {
            mobikulAutoScrollableView.getMyCustomViewPager().setCurrentItem(mobikulAutoScrollableView.getMyCustomViewPager().getCurrentItem() - 1);
            mobikulAutoScrollableView.getMyCustomViewPager().getAdapter().notifyDataSetChanged();
            Log.d(TAG, "onTouch: " + "Left" + mobikulAutoScrollableView.getMyCustomViewPager().getCurrentItem());
        }
    }

    public void clickRight(View v) {
        if (mobikulAutoScrollableView.getMyCustomViewPager().getCurrentItem() != res.length) {
            mobikulAutoScrollableView.getMyCustomViewPager().setCurrentItem(mobikulAutoScrollableView.getMyCustomViewPager().getCurrentItem() + 1);
            mobikulAutoScrollableView.getMyCustomViewPager().getAdapter().notifyDataSetChanged();
            Log.d(TAG, "onTouch: " + "Right" + mobikulAutoScrollableView.getMyCustomViewPager().getCurrentItem());
        }
    }
}