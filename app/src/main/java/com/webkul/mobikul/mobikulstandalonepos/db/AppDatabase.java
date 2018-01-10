package com.webkul.mobikul.mobikulstandalonepos.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.webkul.mobikul.mobikulstandalonepos.db.dao.AdministratorDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.CategoryDao;
import com.webkul.mobikul.mobikulstandalonepos.db.dao.ProductDao;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;

@Database(entities = {Administrator.class, Category.class, Product.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AdministratorDao administratorDao();

    public abstract CategoryDao categoryDao();

    public abstract ProductDao productDao();

    public static final Migration MIGRATION_1_2 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            // Create the new table
            database.execSQL(
                    "CREATE TABLE admin_new (uid INTEGER NOT NULL, first_name TEXT, last_name TEXT, email TEXT, password TEXT, PRIMARY KEY(uid))");
// Copy the data
            database.execSQL(
                    "INSERT INTO admin_new (uid, first_name, last_name, email, password) SELECT uid, first_name, last_name, email, password FROM Administrator");
// Remove the old table
            database.execSQL("DROP TABLE Administrator");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE admin_new RENAME TO Administrator");
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL(
                    "CREATE TABLE Category (cId INTEGER NOT NULL, category_name TEXT, is_active INTEGER NOT NULL, is_include_in_drawer_menu INTEGER NOT NULL, category_icon INTEGER NOT NULL, level INTEGER NOT NULL, parent_id INTEGER NOT NULL, path TEXT, PRIMARY KEY(cId))");
        }
    };
}
