package com.monjya.android.net;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.monjya.android.util.LogManager;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by xmx on 2016/11/28.
 */

public class HttpEntityRequest extends StringRequest {

    private static final String TAG = "HttpEntityRequest";

    private HttpEntity entity;

    public HttpEntityRequest(String url, HttpEntity entity, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(url, entity, Method.POST, listener, errorListener);
    }

    public HttpEntityRequest(String url, HttpEntity entity, int method, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.entity = entity;
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            entity.writeTo(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            LogManager.d(TAG, Log.getStackTraceString(e));
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return new byte[0];
    }
}
