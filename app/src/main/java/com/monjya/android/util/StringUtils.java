package com.monjya.android.util;

import android.content.Context;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class StringUtils {

    public static String md5(String str) {
        MessageDigest md;
        String dstr = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            dstr = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dstr;
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().equals("");
    }

    public static String getText(Context context, int resId) {
        if (context != null && resId > 0) {
            return context.getResources().getString(resId);
        }
        return "";
    }

    public static String join(String str, String[] arr) {
        if (arr == null) {
            return "";
        }
        boolean first = true;
        StringBuilder builder = new StringBuilder();
        for (String e : arr) {
            if (first) {
                first = false;
            } else if (!isBlank(str)) {
                builder.append(str);
            }
            builder.append(e);
        }
        return builder.toString();
    }
}
