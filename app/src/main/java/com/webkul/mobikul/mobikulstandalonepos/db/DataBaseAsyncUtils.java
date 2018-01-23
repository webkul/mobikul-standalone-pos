package com.webkul.mobikul.mobikulstandalonepos.db;

import android.os.AsyncTask;
import android.util.Log;

import com.webkul.mobikul.mobikulstandalonepos.db.converters.DataConverter;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.util.List;

import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.*;

/**
 * Created by aman.gupta on 5/1/18.
 */

public class DataBaseAsyncUtils {
    static DataBaseAsyncUtils dataBaseAsyncUtils;

    public static synchronized DataBaseAsyncUtils getInstanse() {
        if (dataBaseAsyncUtils == null)
            dataBaseAsyncUtils = new DataBaseAsyncUtils();
        return dataBaseAsyncUtils;
    }

    class GetUserAsyncTask extends AsyncTask<Administrator, Void,
            Administrator> {

        private final DataBaseCallBack dataBaseCallBack;
        private AppDatabase db;

        GetUserAsyncTask(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected Administrator doInBackground(Administrator... administrators) {
            Administrator administrator;
            try {
                administrator = db.administratorDao().findByEmail(administrators[0].getEmail(), administrators[0].getPassword());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return administrator;
        }

        @Override
        protected void onPostExecute(Administrator administrator) {
            super.onPostExecute(administrator);
            if (administrator != null)
                dataBaseCallBack.onSuccess(administrator, SUCCESS_MSG_2_SIGN_IN);
            else
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG_2);
        }
    }

    public class AddAdminAsyncTask extends AsyncTask<Administrator, Void,
            Boolean> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public AddAdminAsyncTask(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected Boolean doInBackground(Administrator... administrators) {
            try {
                db.administratorDao().insertAll(administrators);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                dataBaseCallBack.onSuccess(true, SUCCESS_MSG_1_SIGN_UP);
            } else
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
        }
    }

    public class AddCategoryAsyncTask extends AsyncTask<Category, Void,
            Boolean> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public AddCategoryAsyncTask(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected Boolean doInBackground(Category... categories) {
            try {
                db.categoryDao().insertAll(categories);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                dataBaseCallBack.onSuccess(true, SUCCESS_MSG_3_ADD_CATEGORY);
            } else
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
        }
    }

    public class GetCategoryAsyncTask extends AsyncTask<Void, Void,
            List<Category>> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public GetCategoryAsyncTask(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            List<Category> categories = db.categoryDao().getAll();
            return categories;
        }

        @Override
        protected void onPostExecute(List<Category> categoryList) {
            super.onPostExecute(categoryList);
            if (categoryList != null) {
                dataBaseCallBack.onSuccess(categoryList, SUCCESS_MSG);
            } else
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
        }

    }


    public class GetDrawerIncludedCategories extends AsyncTask<Void, Void,
            List<Category>> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public GetDrawerIncludedCategories(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            List<Category> categories = db.categoryDao().getCategoryIncludedInDrawerMenu(true, true);
            return categories;
        }

        @Override
        protected void onPostExecute(List<Category> categoryList) {
            super.onPostExecute(categoryList);
            if (categoryList != null) {
                dataBaseCallBack.onSuccess(categoryList, SUCCESS_MSG);
            } else
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
        }

    }

    public class UpdateCategoryById extends AsyncTask<Category, Void,
            Boolean> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public UpdateCategoryById(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected Boolean doInBackground(Category... categories) {
            try {
                db.categoryDao().updateCategoryById(categories[0].getCategoryName(), categories[0].isActive(), categories[0].isIncludeInDrawerMenu(), categories[0].getCId());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                dataBaseCallBack.onSuccess(aBoolean, SUCCESS_MSG_4_UPDATE_CATEGORY);
            } else {
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
            }
        }
    }

    public class DeleteCategoryById extends AsyncTask<Category, Void,
            Boolean> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public DeleteCategoryById(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected Boolean doInBackground(Category... categories) {
            try {
                db.categoryDao().delete(categories[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                dataBaseCallBack.onSuccess(aBoolean, SUCCESS_MSG_5_DELETE_CATEGORY);
            } else {
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
            }
        }
    }

    public class AddProductAsyncTask extends AsyncTask<Product, Void,
            Boolean> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public AddProductAsyncTask(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected Boolean doInBackground(Product... products) {
            try {
                db.productDao().insertAll(products);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                dataBaseCallBack.onSuccess(true, SUCCESS_MSG_6_ADD_PRODUCT);
            } else
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
        }
    }

    public class GetAllProducts extends AsyncTask<Void, Void,
            List<Product>> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public GetAllProducts(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {
            List<Product> products = db.productDao().getAll();
            return products;
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            super.onPostExecute(products);
            if (products != null) {
                dataBaseCallBack.onSuccess(products, SUCCESS_MSG);
            } else
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
        }
    }

    public class UpdateProduct extends AsyncTask<Product, Void,
            Boolean> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public UpdateProduct(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected Boolean doInBackground(Product... products) {
            try {
                db.productDao().updateProduct(products[0].getImage()
                        , products[0].isEnabled()
                        , products[0].getProductName()
                        , products[0].getSku()
                        , products[0].getPrice()
                        , products[0].getSpecialPrice()
                        , products[0].isTaxableGoodsApplied()
                        , products[0].isTrackInventory()
                        , products[0].getQuantity()
                        , products[0].isStock()
                        , products[0].getWeight()
                        , (new DataConverter()).fromProductCategoriesList(products[0].getProductCategories())
                        , products[0].getPId());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                dataBaseCallBack.onSuccess(aBoolean, SUCCESS_MSG_8_UPDATE_PRODUCT);
            } else {
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
            }
        }
    }


    public class DeleteProduct extends AsyncTask<Product, Void,
            Boolean> {

        private AppDatabase db;
        private final DataBaseCallBack dataBaseCallBack;

        public DeleteProduct(AppDatabase userDatabase, DataBaseCallBack dataBaseCallBack) {
            db = userDatabase;
            this.dataBaseCallBack = dataBaseCallBack;
        }

        @Override
        protected Boolean doInBackground(Product... products) {
            try {
                db.productDao().delete(products[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                dataBaseCallBack.onSuccess(aBoolean, SUCCESS_MSG_7_DELETE_PRODUCT);
            } else {
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
            }
        }
    }

}