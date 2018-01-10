package com.webkul.mobikul.mobikulstandalonepos.db;

import android.content.Context;

import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
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

    public void getAdminData(Context context, Administrator data, DataBaseCallBack dataBaseCallBack) {
        DataBaseAsyncUtils.getInstanse().new GetUserAsyncTask(((BaseActivity) context).getDb(), dataBaseCallBack).execute(data);
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
}
