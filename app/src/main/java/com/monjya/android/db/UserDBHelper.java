package com.monjya.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.monjya.android.app.Monjya;
import com.monjya.android.property.Property;
import com.monjya.android.util.LogManager;

import java.sql.SQLException;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class UserDBHelper extends OrmLiteSqliteOpenHelper {

    public static final String TAG = "UserDBHelper";

    public static final String USER_DATABASE_NAME = "user_:userId_db";

    public static String CURRENT_USER_DATABASE_NAME;

    private static final int DATABASE_VERSION = 1;

    private static UserDBHelper singleton;

    private Dao<Property, Long> propertyDao;

    private UserDBHelper(Context context) {
        super(context, CURRENT_USER_DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized UserDBHelper getHelper() {
        if (singleton == null) {
            Context context = Monjya.getInstance().getApplicationContext();
            singleton = new UserDBHelper(context);
        }
        return singleton;
    }

    public static void releaseHelper() {
        CURRENT_USER_DATABASE_NAME = null;
        try {
            getHelper().close();
        } catch (Throwable throwable) {
        }
        singleton = null;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        if (LogManager.isInfoEnabled()) {
            LogManager.i(TAG, "onCreate");
        }
        try {
            TableUtils.createTable(connectionSource, Property.class);
        } catch (SQLException e) {
            if (LogManager.isErrorEnabled()) {
                LogManager.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (LogManager.isInfoEnabled()) {
            LogManager.i(TAG, "onUpgrade");
        }
        try {
            TableUtils.dropTable(connectionSource, Property.class, true);
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
}
