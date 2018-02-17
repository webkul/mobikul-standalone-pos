package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.adapter.OrderAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentOrdersBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class OrdersFragment extends Fragment {
    FragmentOrdersBinding binding;
    List<OrderEntity> orders;
    private SearchView searchView;
    private ArrayList<OrderEntity> searchOrders;
    private OrderAdapter orderAdapter;

    public static OrdersFragment newInstance(/*String param1, String param2*/) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);
        orders = new ArrayList<>();
        binding.setVisibility(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DataBaseController.getInstanse().getOrders(getActivity(), new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
                if (!responseData.toString().equalsIgnoreCase("[]")) {
                    if (orders.size() > 0)
                        orders.clear();
                    orders.addAll((List<OrderEntity>) responseData);
                    if (orderAdapter == null) {
                        orderAdapter = new OrderAdapter(getActivity(), orders);
                        binding.orderRv.setAdapter(orderAdapter);
                    } else
                        orderAdapter.notifyDataSetChanged();
                    binding.setVisibility(true);
                } else {
                    binding.setVisibility(false);
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                ToastHelper.showToast(getActivity(), errorMsg, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getContext() != null) {
            ((BaseActivity) getContext())
                    .setActionbarTitle(getContext().getString(R.string.orders_fragment_title));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem barcodeItem = menu.findItem(R.id.menu_item_scan_barcode);
        barcodeItem.setVisible(false);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.setVisible(true);
        searchItem.expandActionView();
        searchView.setQueryHint("Search your order...");
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.colorAccent));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchOrders = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    newText = "%" + newText + "%";
                    DataBaseController.getInstanse().getSearchOrders(getActivity(), newText, new DataBaseCallBack() {
                        @Override
                        public void onSuccess(Object responseData, String successMsg) {
                            if (!(searchOrders.toString().equalsIgnoreCase(responseData.toString()))) {
                                if (searchOrders.size() > 0) {
                                    searchOrders.clear();
                                }
                                searchOrders.addAll((List<OrderEntity>) responseData);
                                orderAdapter = new OrderAdapter(getActivity(), searchOrders);
                                binding.orderRv.setAdapter(orderAdapter);
//                                orderAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(int errorCode, String errorMsg) {

                        }
                    });
                    return true;
                } else {
                    orderAdapter = new OrderAdapter(getActivity(), orders);
                    binding.orderRv.setAdapter(orderAdapter);
                }
                return false;
            }
        });

    }
}