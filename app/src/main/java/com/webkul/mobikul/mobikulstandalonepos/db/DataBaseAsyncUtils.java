package com.webkul.mobikul.mobikulstandalonepos.db;

import android.os.AsyncTask;
import android.util.Log;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.util.List;

import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.ERROR_CODE;
import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.ERROR_MSG;
import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.ERROR_MSG_2;

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
            Log.d("data", administrator + "");
            if (administrator != null)
                dataBaseCallBack.onSuccess(administrator);
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
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                dataBaseCallBack.onSuccess(true);
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
                dataBaseCallBack.onSuccess(true);
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
                dataBaseCallBack.onSuccess(categoryList);
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
                dataBaseCallBack.onSuccess(categoryList);
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
                dataBaseCallBack.onSuccess(aBoolean);
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
                dataBaseCallBack.onSuccess(aBoolean);
            } else {
                dataBaseCallBack.onFailure(ERROR_CODE, ERROR_MSG);
            }
        }
    }
}