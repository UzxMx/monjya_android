package com.monjya.android.property;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.monjya.android.db.DBHelper;
import com.monjya.android.util.ListUtils;
import com.monjya.android.util.LogManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class PropertyManager {

    private static final String TAG = "PropertyManager";

    public static final String PROPERTY_CURRENT_ACCOUNT = "current_account";

    public static final String PROPERTY_GUIDES_SHOWN = "guides_shown";

    public static final String PROPERTY_SCHOOLS_SHARED = "schools_shared";

    private static PropertyManager singleton;

    private PropertyManager() {
    }

    public static PropertyManager getInstance() {
        if (singleton == null) {
            synchronized (PropertyManager.class) {
                if (singleton == null) {
                    singleton = new PropertyManager();
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
        DBHelper dbHelper = DBHelper.getHelper();
        Dao<Property, Long> propertyDao = dbHelper.getPropertyDao();

        Property property = query(propertyDao, name);

        return property == null ? "" : property.getValue();
    }

    public void saveProperty(String name, String value) {
        DBHelper dbHelper = DBHelper.getHelper();
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
