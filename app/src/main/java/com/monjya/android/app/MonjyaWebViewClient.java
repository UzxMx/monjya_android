package com.monjya.android.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.monjya.android.util.LogManager;

import java.lang.ref.WeakReference;

/**
 * Created by xmx on 2016/12/14.
 */

public class MonjyaWebViewClient extends WebViewClient {

    private static final String TAG = "MonjyaWebViewClient";

    private WeakReference<Activity> activityWeakReference;

    public MonjyaWebViewClient(Activity activity) {
        activityWeakReference = new WeakReference<Activity>(activity);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return handleUrlLoading(view, request.getUrl().toString());
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return handleUrlLoading(view, url);
    }

    private boolean handleUrlLoading(WebView view, String url) {
        if (LogManager.isDebugEnabled()) {
            LogManager.d(TAG, "url: " + url);
        }

        return false;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (LogManager.isDebugEnabled()) {
            LogManager.d(TAG, "onReceivedError");
        }
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (LogManager.isDebugEnabled()) {
            LogManager.d(TAG, "onReceivedSslError");
        }
        handler.proceed();
    }
}
