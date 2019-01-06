package com.monjya.android.account;

import com.j256.ormlite.dao.Dao;
import com.monjya.android.db.DBHelper;

import java.sql.SQLException;

/**
 * Created by xuemingxiang on 16-11-14.
 */

public class AccountDao {

    public static void save(Account account) {
        DBHelper helper = DBHelper.getHelper();
        Dao<Account, Long> dao = helper.getAccountDao();
        try {
            dao.createOrUpdate(account);
        } catch (SQLException e) {
        }
    }
}
