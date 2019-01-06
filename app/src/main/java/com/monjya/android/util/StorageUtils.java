package com.monjya.android.util;

/**
 * Created by xmx on 2016/12/12.
 */

public class StorageUtils {

    public static String beautifyStorage(long amount) {
        if (amount == 0) {
            return "0";
        }
        if (amount < 1024) {
            return amount + "B";
        }
        if (amount < 1024 * 1024) {
            return amount / 1024 + "K";
        }
        return amount / 1024 / 1024 + "M";
    }
}
