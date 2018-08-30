package com.webkul.mobikul.mobikulstandalonepos.db;

import android.content.Context;

import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.PlaceOrderActivity;
import com.webkul.mobikul.mobikulstandalonepos.customviews.CustomDialogClass;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
//import com.webkul.mobikul.mobikulstandalonepos.db.entity.Tax;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Tax;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

/**
 * Created by aman.gupta on 5/1/18.
 */

public class DataBaseController {
    static DataBaseController dataBaseController;

    public static DataBaseController getInstanse() {
        if (dataBaseController == null)
            dataBaseController = new DataBaseController();
        return dataBaseController;
    }

    public void getAdminDataByEmail(Context context, Administrator data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAdminByEmailAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void getAdminData(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAllAdminAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void updateAdmin(Context context, Administrator administrator, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateAdmin(((BaseActivity) context).getDb(), dataBaseCallBack).execute(administrator);
    }

    public void insertAdministorDetails(Context context, Administrator data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new AddAdminAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void addCategoryDetails(Context context, Category data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new AddCategoryAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void getCategory(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetCategoryAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void getIncludedCategoryForDrawer(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetDrawerIncludedCategories(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void updateCategory(Context context, Category data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateCategoryById(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void deleteCategory(Context context, Category data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new DeleteCategoryById(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void addProduct(Context context, Product data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new AddProductAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void updateProductImages(Context context, String imagePath, Long pId, DataBaseCallBack callBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateProductImages(((BaseActivity) context).getDb(), imagePath, pId, callBack).execute();
    }

    public void getProducts(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAllProducts(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void getAllEnabledProducts(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAllEnabledProducts(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void getAllLowStockProducts(Context context, int minQty, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAllLowStockProducts(((BaseActivity) context).getDb(), dataBaseCallBack).execute(minQty);
    }

    public void updateProduct(Context context, Product data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateProduct(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void updateProductQty(Context context, Product data) {
        DataBaseAsyncUtils.getInstanse().new UpdateProductQty(context, ((BaseActivity) context).getDb()).execute(data);
    }

    public void deleteProduct(Context context, Product data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new DeleteProduct(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void getCustomer(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAllCustomers(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void addCustomer(Context context, Customer data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new AddCustomerAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void updateCustomer(Context context, Customer data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateCustomerAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void deleteCustomer(Context context, Customer customer, DataBaseCallBack callBack) {
        DataBaseAsyncUtils.getInstanse().new DeleteCustomer(((BaseActivity) context).getDb(), callBack).execute(customer);
    }

    public void checkEmailExist(Context context, String email, DataBaseCallBack callBack) {
        DataBaseAsyncUtils.getInstanse().new CheckEmailExist(((BaseActivity) context).getDb(), callBack).execute(email);
    }

    public void checkNumberExist(Context context, String email, DataBaseCallBack callBack) {
        DataBaseAsyncUtils.getInstanse().new CheckNumberExist(((BaseActivity) context).getDb(), callBack).execute(email);
    }

    public void checkSkuExist(Context context, String sku, DataBaseCallBack callBack) {
        DataBaseAsyncUtils.getInstanse().new CheckSkuExist(((BaseActivity) context).getDb(), callBack).execute(sku);
    }

    public void generateOrder(Context context, OrderEntity data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GenerateOrderAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void updateRefundedOrderId(Context context, String returnOrderId, String currentOrderId, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateRefundedOrderId(((BaseActivity) context).getDb(), dataBaseCallBack, returnOrderId, currentOrderId).execute();
    }

    public void getOrders(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetOrders(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void getOrderById(Context context, String orderId, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetOrdersById(((BaseActivity) context).getDb(), dataBaseCallBack).execute(orderId);
    }

    public void getSearchData(Context context, String searchText, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetSearchData(((BaseActivity) context).getDb(), dataBaseCallBack).execute(searchText);
    }

    public void getSearchOrders(Context context, String searchText, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetSearchOrders(((BaseActivity) context).getDb(), dataBaseCallBack).execute(searchText);
    }

    // -------------------- delete all tables -------------------------
    public void deleteAllTables(Context context) {
        DataBaseAsyncUtils.getInstanse().new DeleteAllTables(((BaseActivity) context).getDb()).execute();
    }


    public void addHoldCart(Context context, HoldCart data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new AddCartDataToHoldCart(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void getHoldCart(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetHoldCartData(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void deleteHoldCart(Context context, HoldCart data) {
        DataBaseAsyncUtils.getInstanse().new DeleteHoldCartById(((BaseActivity) context).getDb()).execute(data);
    }

    public void getProductByBarcode(Context context, String data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetProductByBarcode(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void addOpeningBalance(Context context, CashDrawerModel data) {
        DataBaseAsyncUtils.getInstanse().new AddCashDrawerData(((BaseActivity) context).getDb()).execute(data);
    }

    public void updateCashDrawer(Context context, CashDrawerModel cashDrawerModel, DataBaseCallBack callBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateCashData(((BaseActivity) context).getDb(), callBack).execute(cashDrawerModel);
    }

    public void getCashHistoryByDate(Context context, String date, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetCashDrawerDataByDate(((BaseActivity) context).getDb(), dataBaseCallBack).execute(date);
    }

    public void getAllCashHistory(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAllCashDrawerData(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void addOption(Context context, Options options, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new AddOptions(((BaseActivity) context).getDb(), dataBaseCallBack).execute(options);
    }

    public void getOptions(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetOptions(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void updateOptions(Context context, Options data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateOptions(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void deleteOption(Context context, Options data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new DeleteOption(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }

    public void addTaxRate(Context context, Tax options, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new AddTaxRate(((BaseActivity) context).getDb(), dataBaseCallBack).execute(options);
    }

    public void getAllTaxes(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAllTaxes(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void getAllEnabledTaxes(Context context, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetAllEnabledTaxes(((BaseActivity) context).getDb(), dataBaseCallBack).execute();
    }

    public void updateTax(Context context, Tax tax, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new UpdateTaxRate(((BaseActivity) context).getDb(), dataBaseCallBack).execute(tax);
    }

    public void deleteTax(Context context, Tax data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new DeleteTax(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
    }
}
