package com.monjya.android.util;

import java.util.regex.Pattern;

/**
 * Created by xmx on 2017/2/8.
 */

public class ValidationUtils {

    private static final Pattern telephonePattern = Pattern.compile("\\d{11}");

    public static boolean validateTelephone(String text) {
        if (text == null) {
            return false;
        }
        return telephonePattern.matcher(text).matches();
    }
}
