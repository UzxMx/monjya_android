package com.monjya.android.property;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.monjya.android.db.UserDBHelper;
import com.monjya.android.util.ListUtils;
import com.monjya.android.util.LogManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class UserPropertyManager {

    private static final String TAG = "UserPropertyManager";

    public static final String PROPERTY_UNREAD_NOTIFICATIONS = "unread_notifications";

    public static final String PROPERTY_SCHOOSER_RESULT = "schooser_result";

    private static UserPropertyManager singleton;

    public static UserPropertyManager getInstance() {
        if (singleton == null) {
            synchronized (UserPropertyManager.class) {
                if (singleton == null) {
                    singleton = new UserPropertyManager();
                }
            }
        }
        return singleton;
    }

    private Property query(Dao<Property, Long> propertyDao, String name) {
        try {
            List<Property> list = propertyDao.queryForEq("name", name);
            if (!ListUtils.isEmpty(list)) {
                return list.get(0);
            }
        } catch (Throwable throwable) {
            LogManager.e(TAG, Log.getStackTraceString(throwable));
        }
        return null;
    }

    public String getProperty(String name) {
        UserDBHelper dbHelper = UserDBHelper.getHelper();
        Dao<Property, Long> propertyDao = dbHelper.getPropertyDao();

        Property property = query(propertyDao, name);

        return property == null ? "" : property.getValue();
    }

    public void saveProperty(String name, String value) {
        UserDBHelper dbHelper = UserDBHelper.getHelper();
        Dao<Property, Long> propertyDao = dbHelper.getPropertyDao();

        try {
            Property property = query(propertyDao, name);
            if (property == null) {
                property = new Property(name, value);
            } else {
                property.setValue(value);
            }
            propertyDao.createOrUpdate(property);
        } catch (SQLException e) {
            LogManager.e(TAG, Log.getStackTraceString(e));
        }
    }
}
