package com.monjya.android.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xmx on 2017/3/12.
 */

public abstract class AbstractRequest<T> extends Request<T> {

    private Map<String, String> headers = new HashMap<>();

    public AbstractRequest(String url, Response.ErrorListener listener) {
        super(url, listener);
    }

    public AbstractRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    public AbstractRequest<T> addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }
}
