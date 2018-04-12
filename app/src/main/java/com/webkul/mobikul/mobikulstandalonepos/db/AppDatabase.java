package com.webkul.mobikul.mobikulstandalonepos.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

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
import com.webkul.mobikul.mobikulstandalonepos.db.dao.TaxDao;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Tax;
//import com.webkul.mobikul.mobikulstandalonepos.sqlAsset.AssetSQLiteOpenHelperFactory;

@Database(entities = {Administrator.class, Category.class, Product.class, Customer.class, OrderEntity.class
        , HoldCart.class, CashDrawerModel.class, Options.class, OptionValues.class, Tax.class/*, Currency.class*/}, version = 18, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mINSTANCE;

    public abstract AdministratorDao administratorDao();

    public abstract CategoryDao categoryDao();

    public abstract ProductDao productDao();

    public abstract CustomerDao customerDao();

    public abstract OrderDao orderDao();

    public abstract HoldCartDao holdCartDao();

    public abstract CashDrawerDao cashDrawerDao();

    public abstract OptionDao optionDao();

//    public abstract CurrencyDao currencyDao();

    public abstract TaxDao taxDao();

    public abstract OptionValuesDao optionValuesDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (mINSTANCE == null) {
            mINSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "db_pos.db")
                            .addMigrations(MIGRATION_17_18)
                            .build();
        }
        return mINSTANCE;
    }

    public static final Migration MIGRATION_17_1 = new Migration(17, 1) {
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
            database.execSQL(
                    "INSERT INTO Category_new (cId, category_name, is_active, is_include_in_drawer_menu, category_icon, level, parent_id, path) SELECT cId, category_name, is_active, is_include_in_drawer_menu, category_icon, level, parent_id, path FROM Category");
            database.execSQL("DROP TABLE Category");
            database.execSQL("ALTER TABLE Category_new RENAME TO Category");

            // Create the new table
            database.execSQL(
                    "CREATE TABLE Product_new (pId INTEGER NOT NULL, product_name TEXT, product_s_deccription TEXT, sku TEXT, is_enabled INTEGER NOT NULL, price TEXT, special_price TEXT, is_taxable_goods_applied INTEGER NOT NULL" +
                            ",track_inventory INTEGER NOT NULL, quantity TEXT, stock_availability INTEGER NOT NULL, image TEXT, weight TEXT, productCategories TEXT,formatted_price TEXT, formatted_special_price TEXT" +
                            ",barCode TEXT, product_tax TEXT, options TEXT, PRIMARY KEY(pId))");
            database.execSQL(
                    "INSERT INTO Product_new (pId, product_name, product_s_deccription, sku, is_enabled, price, special_price, is_taxable_goods_applied, track_inventory, quantity, stock_availability, image, weight, productCategories" +
                            ", formatted_price, formatted_special_price, barCode, product_tax, options )" +
                            " SELECT pId, product_name, product_s_deccription, sku, is_enabled, price, special_price, is_taxable_goods_applied, track_inventory" +
                            ", quantity, stock_availability, image, weight,productCategories, formatted_price, formatted_special_price, barCode, product_tax, options " +
                            " FROM Product");
            database.execSQL("DROP TABLE Product");
            database.execSQL("ALTER TABLE Product_new RENAME TO Product");

            //create customer table
            database.execSQL(
                    "CREATE TABLE Customer_new (customerId INTEGER NOT NULL, customer_first_name TEXT, customer_last_name TEXT, email TEXT, contact_number TEXT" +
                            ", address_line TEXT,city TEXT,postal_code TEXT,state TEXT,country TEXT, PRIMARY KEY(customerId))");
            database.execSQL(
                    "INSERT INTO Customer_new (customerId, customer_first_name, customer_last_name, email, contact_number, address_line, city, postal_code,state" +
                            ", country) SELECT customerId, customer_first_name, customer_last_name, email, contact_number, address_line, city, postal_code,state" +
                            ", country FROM Customer");
            database.execSQL("DROP TABLE Customer");
            database.execSQL("ALTER TABLE Customer_new RENAME TO Customer");

            //create OrderEntity table
            database.execSQL(
                    "CREATE TABLE OrderEntity_new (orderId INTEGER NOT NULL, time TEXT, date TEXT, cart_data TEXT, qty TEXT" +
                            ", cash_data TEXT,is_synced TEXT,is_return TEXT,refunded_order_id TEXT, PRIMARY KEY(orderId))");
            database.execSQL(
                    "INSERT INTO OrderEntity_new (orderId, time, date, cart_data, qty, cash_data, is_synced, is_return,refunded_order_id) " +
                            "SELECT orderId, time, date, cart_data, qty, cash_data, is_synced, is_return,refunded_order_id FROM OrderEntity");
            database.execSQL("DROP TABLE OrderEntity");
            database.execSQL("ALTER TABLE OrderEntity_new RENAME TO OrderEntity");

            //create HoldCart table
            database.execSQL(
                    "CREATE TABLE HoldCart_new (holdCartId INTEGER NOT NULL, time TEXT, date TEXT, cart_data TEXT, qty TEXT" +
                            ", is_synced TEXT, PRIMARY KEY(holdCartId))");
            database.execSQL(
                    "INSERT INTO HoldCart_new (holdCartId, time, date, cart_data, qty, is_synced) " +
                            "SELECT holdCartId, time, date, cart_data, qty, is_synced FROM HoldCart");
            database.execSQL("DROP TABLE HoldCart");
            database.execSQL("ALTER TABLE HoldCart_new RENAME TO HoldCart");

            //create CashDrawerModel table
            database.execSQL(
                    "CREATE TABLE CashDrawerModel_new (date INTEGER NOT NULL, cash_drawer_items TEXT, opening_balance TEXT, formatted_opening_balance TEXT" +
                            ", closing_balance TEXT, formatted_closing_balance TEXT, net_revenue TEXT, formatted_net_revenue TEXT, " +
                            ", in_amount TEXT, formatted_in_amount TEXT, out_amount TEXT, formatted_out_amount TEXT, is_synced TEXT, PRIMARY KEY(date))");
            database.execSQL(
                    "INSERT INTO CashDrawerModel_new (date, cash_drawer_items, opening_balance, formatted_opening_balance, closing_balance, formatted_closing_balance, net_revenue, formatted_net_revenue, in_amount, formatted_in_amount, out_amount, formatted_out_amount, is_synced) " +
                            "SELECT date, cash_drawer_items, opening_balance, formatted_opening_balance, closing_balance, formatted_closing_balance, net_revenue, formatted_net_revenue, in_amount, formatted_in_amount, out_amount, formatted_out_amount, is_synced FROM CashDrawerModel");
            database.execSQL("DROP TABLE CashDrawerModel");
            database.execSQL("ALTER TABLE CashDrawerModel_new RENAME TO CashDrawerModel");

            //create HoldCart table
            database.execSQL(
                    "CREATE TABLE Option_new (optionId INTEGER NOT NULL, option_name TEXT, option_type TEXT, option_values TEXT, sort_order TEXT" +
                            ", PRIMARY KEY(optionId))");
            database.execSQL(
                    "INSERT INTO Option_new (optionId, option_name, option_type, option_values, sort_order) " +
                            "SELECT optionId, option_name, option_type, option_values, sort_order FROM Option");
            database.execSQL("DROP TABLE Option");
            database.execSQL("ALTER TABLE Option_new RENAME TO HoldCart");

            //create Tax table
            database.execSQL(
                    "CREATE TABLE Tax_new (taxId INTEGER NOT NULL, tax_name TEXT, is_enabled TEXT, type TEXT, tax_rate TEXT" +
                            ", PRIMARY KEY(taxId))");
            database.execSQL(
                    "INSERT INTO Tax_new (taxId, tax_name, is_enabled, type, tax_rate) " +
                            "SELECT taxId, tax_name, is_enabled, type, tax_rate FROM Tax");
            database.execSQL("DROP TABLE Tax");
            database.execSQL("ALTER TABLE Tax_new RENAME TO Tax");
        }
    };

    public static final Migration MIGRATION_17_18 = new Migration(17, 18) {
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
            database.execSQL(
                    "INSERT INTO Category_new (cId, category_name, is_active, is_include_in_drawer_menu, category_icon, level, parent_id, path) SELECT cId, category_name, is_active, is_include_in_drawer_menu, category_icon, level, parent_id, path FROM Category");
            database.execSQL("DROP TABLE Category");
            database.execSQL("ALTER TABLE Category_new RENAME TO Category");

            // Create the new table
            database.execSQL(
                    "CREATE TABLE Product_new (pId INTEGER NOT NULL, product_name TEXT, product_s_deccription TEXT, sku TEXT, is_enabled INTEGER NOT NULL, price TEXT, special_price TEXT, is_taxable_goods_applied INTEGER NOT NULL" +
                            ",track_inventory INTEGER NOT NULL, quantity TEXT, stock_availability INTEGER NOT NULL, image TEXT, weight TEXT, productCategories TEXT,formatted_price TEXT, formatted_special_price TEXT" +
                            ",barCode TEXT, product_tax TEXT, options TEXT, discount REAL NOT NULL DEFAULT 0, formatted_discount TEXT, PRIMARY KEY(pId))");
            database.execSQL(
                    "INSERT INTO Product_new (pId, product_name, product_s_deccription, sku, is_enabled, price, special_price, is_taxable_goods_applied, track_inventory, quantity, stock_availability, image, weight, productCategories" +
                            ", formatted_price, formatted_special_price, barCode, product_tax, options )" +
                            " SELECT pId, product_name, product_s_deccription, sku, is_enabled, price, special_price, is_taxable_goods_applied, track_inventory" +
                            ", quantity, stock_availability, image, weight,productCategories, formatted_price, formatted_special_price, barCode, product_tax, options " +
                            " FROM Product");
            database.execSQL("DROP TABLE Product");
            database.execSQL("ALTER TABLE Product_new RENAME TO Product");

            //create customer table
            database.execSQL(
                    "CREATE TABLE Customer_new (customerId INTEGER NOT NULL, customer_first_name TEXT, customer_last_name TEXT, email TEXT, contact_number TEXT" +
                            ", address_line TEXT,city TEXT,postal_code TEXT,state TEXT,country TEXT, PRIMARY KEY(customerId))");
            database.execSQL(
                    "INSERT INTO Customer_new (customerId, customer_first_name, customer_last_name, email, contact_number, address_line, city, postal_code,state" +
                            ", country) SELECT customerId, customer_first_name, customer_last_name, email, contact_number, address_line, city, postal_code,state" +
                            ", country FROM Customer");
            database.execSQL("DROP TABLE Customer");
            database.execSQL("ALTER TABLE Customer_new RENAME TO Customer");

            //create OrderEntity table
            database.execSQL(
                    "CREATE TABLE OrderEntity_new (orderId INTEGER NOT NULL, time TEXT, date TEXT, cart_data TEXT, qty TEXT" +
                            ", cash_data TEXT,is_synced TEXT,is_return INTEGER NOT NULL, refunded_order_id TEXT, PRIMARY KEY(orderId))");
            database.execSQL(
                    "INSERT INTO OrderEntity_new (orderId, time, date, cart_data, qty, cash_data, is_synced, is_return,refunded_order_id) " +
                            "SELECT orderId, time, date, cart_data, qty, cash_data, is_synced, is_return,refunded_order_id FROM OrderEntity");
            database.execSQL("DROP TABLE OrderEntity");
            database.execSQL("ALTER TABLE OrderEntity_new RENAME TO OrderEntity");

            //create HoldCart table
            database.execSQL(
                    "CREATE TABLE HoldCart_new (holdCartId INTEGER NOT NULL, time TEXT, date TEXT, cart_data TEXT, qty TEXT" +
                            ", is_synced TEXT, PRIMARY KEY(holdCartId))");
            database.execSQL(
                    "INSERT INTO HoldCart_new (holdCartId, time, date, cart_data, qty, is_synced) " +
                            "SELECT holdCartId, time, date, cart_data, qty, is_synced FROM HoldCart");
            database.execSQL("DROP TABLE HoldCart");
            database.execSQL("ALTER TABLE HoldCart_new RENAME TO HoldCart");

            //create CashDrawerModel table
//            database.execSQL(
//                    "CREATE TABLE CashDrawerModel_new (date INTEGER NOT NULL, cash_drawer_items TEXT, opening_balance TEXT, formatted_opening_balance TEXT" +
//                            ", closing_balance TEXT, formatted_closing_balance TEXT, net_revenue TEXT, formatted_net_revenue TEXT, " +
//                            ", in_amount TEXT, formatted_in_amount TEXT, out_amount TEXT, formatted_out_amount TEXT, is_synced TEXT, PRIMARY KEY(date))");
//            database.execSQL(
//                    "INSERT INTO CashDrawerModel_new (date, cash_drawer_items, opening_balance, formatted_opening_balance, closing_balance, formatted_closing_balance, net_revenue, formatted_net_revenue, in_amount, formatted_in_amount, out_amount, formatted_out_amount, is_synced) " +
//                            "SELECT date, cash_drawer_items, opening_balance, formatted_opening_balance, closing_balance, formatted_closing_balance, net_revenue, formatted_net_revenue, in_amount, formatted_in_amount, out_amount, formatted_out_amount, is_synced FROM CashDrawerModel");
//            database.execSQL("DROP TABLE CashDrawerModel");
//            database.execSQL("ALTER TABLE CashDrawerModel_new RENAME TO CashDrawerModel");

            //create HoldCart table
//            database.execSQL(
//                    "CREATE TABLE Option_new (optionId INTEGER NOT NULL, option_name TEXT, option_type TEXT, option_values TEXT, sort_order TEXT" +
//                            ", PRIMARY KEY(optionId))");
//            database.execSQL(
//                    "INSERT INTO Option_new (optionId, option_name, option_type, option_values, sort_order) " +
//                            "SELECT optionId, option_name, option_type, option_values, sort_order FROM Option");
//            database.execSQL("DROP TABLE Option");
//            database.execSQL("ALTER TABLE Option_new RENAME TO HoldCart");
//
//            //create Tax table
//            database.execSQL(
//                    "CREATE TABLE Tax_new (taxId INTEGER NOT NULL, tax_name TEXT, is_enabled TEXT, type TEXT, tax_rate TEXT" +
//                            ", PRIMARY KEY(taxId))");
//            database.execSQL(
//                    "INSERT INTO Tax_new (taxId, tax_name, is_enabled, type, tax_rate) " +
//                            "SELECT taxId, tax_name, is_enabled, type, tax_rate FROM Tax");
//            database.execSQL("DROP TABLE Tax");
//            database.execSQL("ALTER TABLE Tax_new RENAME TO Tax");
        }
    };

    public static void destroyDbInstance() {
        mINSTANCE = null;
    }

}
