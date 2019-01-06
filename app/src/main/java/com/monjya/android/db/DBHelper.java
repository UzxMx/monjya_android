package com.monjya.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.monjya.android.account.Account;
import com.monjya.android.app.Monjya;
import com.monjya.android.property.Property;
import com.monjya.android.util.LogManager;

import java.sql.SQLException;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "xinya_db";

    private static final int DATABASE_VERSION = 2;

    private static DBHelper singleton;

    private Dao<Property, Long> propertyDao;

    private Dao<Account, Long> accountDao;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHelper getHelper() {
        if (singleton == null) {
            Context context = Monjya.getInstance().getApplicationContext();
            singleton = new DBHelper(context);
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        if (LogManager.isInfoEnabled()) {
            LogManager.i(TAG, "onCreate");
        }
        try {
            TableUtils.createTable(connectionSource, Property.class);
            TableUtils.createTable(connectionSource, Account.class);
        } catch (SQLException e) {
            if (LogManager.isErrorEnabled()) {
                LogManager.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

    // TODO verify that after updating app, user is logged out auto
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (LogManager.isInfoEnabled()) {
            LogManager.i(TAG, "onUpgrade, oldVersion: " + oldVersion + ", newVersion: " + newVersion);
        }
        try {
            TableUtils.dropTable(connectionSource, Property.class, true);
            TableUtils.dropTable(connectionSource, Account.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            if (LogManager.isErrorEnabled()) {
                LogManager.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

    public Dao<Property, Long> getPropertyDao() {
        if (propertyDao == null) {
            synchronized (this) {
                if (propertyDao == null) {
                    try {
                        propertyDao = getDao(Property.class);
                    } catch (SQLException e) {
                        LogManager.e(TAG, Log.getStackTraceString(e));
                    }
                }
            }
        }
        return propertyDao;
    }

    public Dao<Account, Long> getAccountDao() {
        if (accountDao == null) {
            synchronized (this) {
                if (accountDao == null) {
                    try {
                        accountDao = getDao(Account.class);
                    } catch (SQLException e) {
                        LogManager.e(TAG, Log.getStackTraceString(e));
                    }
                }
            }
        }
        return accountDao;
    }
}
