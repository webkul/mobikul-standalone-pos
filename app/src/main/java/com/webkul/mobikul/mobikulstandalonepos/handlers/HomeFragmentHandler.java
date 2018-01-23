package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CartActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.HomeFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman.gupta on 17/1/18. @Webkul Software Private limited
 */

public class HomeFragmentHandler {

    private Context context;
    double subTotal;
    int counter;
    String currencySymbol;
    private double grandTotal;
    DecimalFormat df;

    public HomeFragmentHandler(Context context) {
        this.context = context;
        currencySymbol = context.getResources().getString(R.string.currency_symbol);
        df = new DecimalFormat("####0.00");
    }

    public void onClickProduct(Product product) {
        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());
        CartModel cartData = ((HomeFragment) fragment).binding.getCartData();
        subTotal = Double.parseDouble(cartData.getTotals().getSubTotal());
        counter = Integer.parseInt(cartData.getTotals().getQty());
        if (product.getSpecialPrice().isEmpty())
            subTotal = subTotal + Double.parseDouble(product.getPrice());
        else
            subTotal = subTotal + Double.parseDouble(product.getSpecialPrice());
        counter++;

        grandTotal = subTotal + Float.parseFloat(cartData.getTotals().getTax());
        boolean isProductAlreadyInCart = false;

        int position = -1;
        for (Product product1 : cartData.getProducts()) {
            position++;
            if (product.getPId() == product1.getPId()) {
                int cartQty = Integer.parseInt(product1.getCartQty());
                cartQty++;
                product.setCartQty(cartQty + "");
                isProductAlreadyInCart = true;
                break;
            }
        }

        if (!isProductAlreadyInCart)
            cartData.getProducts().add(product);
        else {
            cartData.getProducts().remove(position);
            cartData.getProducts().add(position, product);
        }

//         // decimal format
//        String formatedSubtotal = df.format(subTotal);
//        String formatedGrandTotal = df.format(grandTotal);

        cartData.getTotals().setSubTotal(df.format(subTotal)+"");
        cartData.getTotals().setQty(counter + "");
        cartData.getTotals().setTax(cartData.getTotals().getTax());
        cartData.getTotals().setGrandTotal(df.format(grandTotal) + "");
        cartData.getTotals().setRoundTotal(Math.ceil(grandTotal) + "");
        // set formated values
        cartData.getTotals().setFormatedSubTotal(currencySymbol + df.format(subTotal) + "");
        cartData.getTotals().setFormatedTax(currencySymbol + "" + cartData.getTotals().getTax());
        cartData.getTotals().setFormatedGrandTotal(currencySymbol + df.format(grandTotal) + "");
        cartData.getTotals().setFormatedRoundTotal(currencySymbol + Math.ceil(grandTotal) + "");
        AppSharedPref.setCartData(context, Helper.fromCartModelToString(cartData));
    }

    public void goToCart(CartModel cartData) {
        Intent i = new Intent(context, CartActivity.class);
        i.putExtra("cartData", Helper.fromCartModelToString(cartData));
        context.startActivity(i);
    }
}
