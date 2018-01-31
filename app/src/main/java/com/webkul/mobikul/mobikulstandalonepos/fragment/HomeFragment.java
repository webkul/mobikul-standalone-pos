package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.adapter.HomePageProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentHomeBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.HomeFragmentHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class HomeFragment extends Fragment {
    public List<Product> products;
    public List<Product> searchProduct;
    private List<Product> selectedProductsByCategory;
    public HomePageProductAdapter productAdapter;
    private HomePageProductAdapter productAdapterForSelectedCategory;
    public FragmentHomeBinding binding;
    private SearchView searchView;
    private String ARG_PARAM1 = "category_id";
    private String cId;

    public static HomeFragment newInstance(/*String param1, String param2*/) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null && getArguments().containsKey("category_id")) {
            cId = getArguments().getString(ARG_PARAM1);
        }
    }

    public void loadHomeProduct() {
        products = new ArrayList<>();
        setProduct();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        loadHomeProduct();
        loadCartData();

        binding.setHandler(new HomeFragmentHandler(getContext()));
        return binding.getRoot();
    }

    private void loadCartData() {
        CartModel cartData;
        if (AppSharedPref.getCartData(getActivity()).isEmpty())
            cartData = new CartModel();
        else
            cartData = Helper.fromStringToCartModel(AppSharedPref.getCartData(getActivity()));
        binding.setCartData(cartData);
    }

    public void setProduct() {
        DataBaseController.getInstanse().getProducts(getActivity(), new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String msg) {
                if (!(products.toString().equalsIgnoreCase(responseData.toString()))) {
                    if (products.size() > 0)
                        products.clear();
                    products.addAll((List<Product>) responseData);
                    if (productAdapter == null) {
                        productAdapter = new HomePageProductAdapter(getActivity(), products);
                        binding.productRv.setAdapter(productAdapter);
                    } else {
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Toast.makeText(getActivity(), errorMsg + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showCategorySelectedProducts(String cId) {
        if (selectedProductsByCategory == null)
            selectedProductsByCategory = new ArrayList<>();
        if (selectedProductsByCategory.size() > 0)
            selectedProductsByCategory.clear();
        for (Product product : products) {
            for (ProductCategoryModel pro : product.getProductCategories()) {
                if (cId.equalsIgnoreCase(pro.getcId())) {
                    selectedProductsByCategory.add(product);
                    break;
                }
            }
        }
        if (productAdapterForSelectedCategory == null) {
            productAdapterForSelectedCategory = new HomePageProductAdapter(getActivity(), selectedProductsByCategory);
            binding.productRv.setAdapter(productAdapterForSelectedCategory);
        } else {
            productAdapterForSelectedCategory.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getContext() != null) {
            ((BaseActivity) getContext())
                    .setActionbarTitle(getContext().getString(R.string.home_fragment_title));
        }
        setProduct();
        loadCartData();
        if (cId != null)
            showCategorySelectedProducts(cId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.setVisible(true);
        searchItem.expandActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchProduct = new ArrayList<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    newText = "%" + newText + "%";
                    DataBaseController.getInstanse().getSearchData(getActivity(), newText, new DataBaseCallBack() {
                        @Override
                        public void onSuccess(Object responseData, String successMsg) {
                            Log.d(TAG, "onSuccess: " + " size-" + ((List<Product>) responseData).size());
                            if (!(searchProduct.toString().equalsIgnoreCase(responseData.toString()))) {
                                if (searchProduct.size() > 0) {
                                    searchProduct.clear();
                                    productAdapter = null;
                                }
                                searchProduct.addAll((List<Product>) responseData);
                                if (productAdapter == null) {
                                    productAdapter = new HomePageProductAdapter(getActivity(), searchProduct);
                                    binding.productRv.setAdapter(productAdapter);
                                } else {
                                    productAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int errorCode, String errorMsg) {

                        }
                    });
                    return true;
                } else {
                    productAdapter = new HomePageProductAdapter(getActivity(), products);
                    binding.productRv.setAdapter(productAdapter);
                }
                return false;
            }
        });

    }

}