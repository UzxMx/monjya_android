package com.monjya.android.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by xuemingxiang on 16-11-11.
 */

public class UnitUtils {

    public static int convertDpToPixel(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static int convertSpToPixel(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }
}
