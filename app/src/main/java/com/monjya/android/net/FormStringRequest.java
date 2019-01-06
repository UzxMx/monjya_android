package com.monjya.android.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuemingxiang on 16-11-14.
 */

public class FormStringRequest extends StringRequest {

    private static final int DEFAULT_TIMEOUT_MS = 10 * 1000;

    private static final int DEFAULT_MAX_RETRIES = 2;

    private static final int DEFAULT_BACKOFF_MULT = 1;

    private Map<String, String> map = new HashMap<>();

    public FormStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);

        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
    }

    public FormStringRequest addParam(String key, String value) {
        map.put(key, value);
        return this;
    }

    public FormStringRequest addParam(String key, int value) {
        return addParam(key, Integer.toString(value));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
