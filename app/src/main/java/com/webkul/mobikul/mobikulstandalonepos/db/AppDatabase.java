package com.webkul.mobikul.mobikulstandalonepos.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

import com.webkul.mobikul.mobikulstandalonepos.db.converters.DataConverter;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.AdministratorDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.CashDrawerDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.CategoryDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.CustomerDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.HoldCartDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.OptionDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.OptionValuesDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.OrderDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.ProductDao;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;

@Database(entities = {Administrator.class, Category.class, Product.class, Customer.class, OrderEntity.class
        , HoldCart.class, CashDrawerModel.class, Options.class, OptionValues.class}, version = 9, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AdministratorDao administratorDao();

    public abstract CategoryDao categoryDao();

    public abstract ProductDao productDao();

    public abstract CustomerDao customerDao();

    public abstract OrderDao orderDao();

    public abstract HoldCartDao holdCartDao();

    public abstract CashDrawerDao cashDrawerDao();

    public abstract OptionDao optionDao();

    public abstract OptionValuesDao optionValuesDao();

    public static final Migration MIGRATION_1_2 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            // Create the new table
            database.execSQL(
                    "CREATE TABLE admin_new (uid INTEGER NOT NULL, first_name TEXT, last_name TEXT, email TEXT,username TEXT, password TEXT, PRIMARY KEY(uid))");
// Copy the data
            database.execSQL(
                    "INSERT INTO admin_new (uid, first_name, last_name, email, password) SELECT uid, first_name, last_name, email, password FROM Administrator");
// Remove the old table
            database.execSQL("DROP TABLE Administrator");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE admin_new RENAME TO Administrator");
            // Since we didn't alter the table, there's nothing else to do here.

            // Create the new table
            database.execSQL(
                    "CREATE TABLE Category_new (cId INTEGER NOT NULL, category_name TEXT, is_active INTEGER NOT NULL, is_include_in_drawer_menu INTEGER NOT NULL, category_icon INTEGER NOT NULL, level INTEGER NOT NULL, parent_id INTEGER NOT NULL, path TEXT, PRIMARY KEY(cId))");
// Copy the data
            database.execSQL(
                    "INSERT INTO Category_new (cId, category_name, is_active, is_include_in_drawer_menu, category_icon, level, parent_id, path) SELECT cId, category_name, is_active, is_include_in_drawer_menu, category_icon, level, parent_id, path FROM Category");
            // Remove the old table
            database.execSQL("DROP TABLE Category");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE Category_new RENAME TO Category");
//             Since we didn't alter the table, there's nothing else to do here.
            // Create the new table
            database.execSQL(
                    "CREATE TABLE Product_new (pId INTEGER NOT NULL, product_name TEXT, product_s_deccription TEXT, sku TEXT, is_enabled INTEGER NOT NULL, price TEXT, special_price TEXT, is_taxable_goods_applied INTEGER NOT NULL" +
                            ",track_inventory INTEGER NOT NULL, quantity TEXT, stock_availability INTEGER NOT NULL, image TEXT, weight TEXT, productCategories TEXT,formatted_price TEXT, formatted_special_price TEXT, PRIMARY KEY(pId))");
// Copy the data
            database.execSQL(
                    "INSERT INTO Product_new (pId, product_name, product_s_deccription, sku, is_enabled, price, special_price, is_taxable_goods_applied, track_inventory, quantity, stock_availability, image, weight, productCategories, formatted_price, formatted_special_price )" +
                            " SELECT pId, product_name, product_s_deccription, sku, is_enabled, price, special_price, is_taxable_goods_applied, track_inventory, quantity, stock_availability, image, weight,productCategories, formatted_price, formatted_special_price " +
                            " FROM Product");
            // Remove the old table
            database.execSQL("DROP TABLE Product");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE Product_new RENAME TO Product");

//create customer table
            database.execSQL("DROP TABLE Customer");

            database.execSQL(
                    "CREATE TABLE Customer (customerId INTEGER NOT NULL, customer_first_name TEXT, customer_last_name TEXT, email TEXT, contact_number TEXT, PRIMARY KEY(customerId))");

//            database.execSQL("DROP TABLE OrderEntity");

//            database.execSQL(
//                    "CREATE TABLE OrderEntity (orderId INTEGER NOT NULL, cash_data TEXT, cart_data TEXT, time TEXT, date TEXT, qty TEXT, is_synced TEXT, PRIMARY KEY(orderId))");


//            database.execSQL("ALTER TABLE Product ADD COLUMN formatted_price TEXT");
        }
    };

}
