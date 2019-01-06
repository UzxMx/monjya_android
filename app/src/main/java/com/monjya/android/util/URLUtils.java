package com.monjya.android.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class URLUtils {

    private static final String TAG = "URLUtils";

    public static String addQueryParams(String url, Map<String, String> queryParams) {
        boolean first = true;
        StringBuilder builder = new StringBuilder(url);
        try {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (first) {
                    builder.append('?');
                    first = false;
                } else {
                    builder.append('&');
                }

                builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            LogManager.e(TAG, Log.getStackTraceString(e));
        }
        return builder.toString();
    }
}
