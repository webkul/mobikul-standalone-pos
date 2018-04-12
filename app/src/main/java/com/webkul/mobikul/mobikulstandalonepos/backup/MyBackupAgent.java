package com.webkul.mobikul.mobikulstandalonepos.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;

import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;

import java.io.IOException;

import static com.webkul.mobikul.mobikulstandalonepos.helper.Helper.DB_NAME;
import static com.webkul.mobikul.mobikulstandalonepos.helper.Helper.DB_NAME_IMAGES;
import static com.webkul.mobikul.mobikulstandalonepos.helper.Helper.DB_PATH;

public class MyBackupAgent extends BackupAgentHelper {

    // The name of the SharedPreferences file
    static final String PREFS = AppSharedPref.APP_PREF;
    static final String USER_PREFS = AppSharedPref.USER_PREF;
    static final String dBFile = DB_PATH + DB_NAME;
//    static final String dBImageFile = DB_PATH + DB_NAME_IMAGES;

    /**
     * We serialize access to our persistent data through a global static
     * object.  This ensures that in the unlikely event of the our backup/restore
     * agent running to perform a backup while our UI is updating the file, the
     * agent will not accidentally read partially-written data.
     * <p>
     * <p>Curious but true: a zero-length array is slightly lighter-weight than
     * merely allocating an Object, and can still be synchronized on.
     */
    static final Object[] sDataLock = new Object[0];
    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = AppSharedPref.APP_PREF;

    @Override
    public void onCreate() {
//        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS, USER_PREFS);
//        addHelper(PREFS_BACKUP_KEY, helper);
        FileBackupHelper fileBackupHelper = new FileBackupHelper(this, dBFile);
        addHelper("dbFile", fileBackupHelper);
//        FileBackupHelper fileBackupHelper1 = new FileBackupHelper(this, dBImageFile);
//        addHelper("dbFile", fileBackupHelper1);
        new BackupManager(getApplicationContext()).dataChanged();
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                         ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper performs the backup operation
        synchronized (sDataLock) {
            super.onBackup(oldState, data, newState);
        }
    }

    /**
     * Adding locking around the file rewrite that happens during restore is
     * similarly straightforward.
     */
    @Override
    public void onRestore(BackupDataInput data, int appVersionCode,
                          ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper restores the file from
        // the data provided here.
        synchronized (sDataLock) {
            super.onRestore(data, appVersionCode, newState);
        }
    }
}