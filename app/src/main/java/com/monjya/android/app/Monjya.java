package com.monjya.android.app;

import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;

import com.monjya.android.account.AccountManager;
import com.monjya.android.util.LogManager;
import com.monjya.android.util.StreamUtils;
import com.monjya.android.util.StringUtils;
import com.squareup.picasso.CacheUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by xmx on 2017/2/12.
 */

public class Monjya {
    private static final String TAG = "Monjya";

    public static final int PAGE_SIZE = 10;

    public static long CLICK_BACK_BTN_INTERVAL = 2000;

    private static Monjya singleton;

    private WeakReference<Context> applicationContextWeakRef;

    private Handler handler;

    private Monjya() {
    }

    public static Monjya getInstance() {
        if (singleton == null) {
            synchronized (Monjya.class) {
                if (singleton == null) {
                    singleton = new Monjya();
                }
            }
        }
        return singleton;
    }

    public void init(Context applicationContext) {
        LogManager.init(true);

        CacheUtils.initCache(applicationContext, 100 * 1024 * 1024);

        applicationContextWeakRef = new WeakReference<Context>(applicationContext);
        handler = new Handler();

        AccountManager.getInstance().init();
    }

    public Context getApplicationContext() {
        if (applicationContextWeakRef != null) {
            return applicationContextWeakRef.get();
        }
        return null;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public static void setHtmlToWebView(String html, WebView webView) {
        webView.loadData(html, "text/html; charset=UTF-8", null);
    }

    public static void setHtmlToWebViewWithStyle(String html, WebView webView) {
        setHtmlToWebViewWithStyle(html, new String[]{"style.css"}, webView);
    }

    public static void setHtmlToWebViewWithStyle(String html, String bodyClass, WebView webView) {
        setHtmlToWebViewWithStyle(html, new String[]{"style.css"}, bodyClass, webView);
    }

    public static void setHtmlToWebViewWithStyle(String html, String[] styles, WebView webView) {
        setHtmlToWebViewWithStyle(html, styles, null, webView);
    }

    public static void setHtmlToWebViewWithStyle(String html, String[] styles, String bodyClass, WebView webView) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><head>");
        builder.append("<style>");
        for (String style : styles) {
            try {
                InputStream inputStream = getInstance().getApplicationContext().getAssets().open(style);
                builder.append(StreamUtils.readInputStreamAndClose(inputStream));
            } catch (IOException e) {
            }
        }
        builder.append("</style>");
        builder.append("</head>");
        if (StringUtils.isBlank(bodyClass)) {
            builder.append("<body>");
        } else {
            builder.append("<body class=\"" + bodyClass + "\">");
        }
        builder.append(html);
        builder.append("</body></html");
        webView.loadData(builder.toString(), "text/html; charset=UTF-8", "UTF-8");
    }

    public static int calculatePage(int offset) {
        return offset / PAGE_SIZE + 1;
    }
}
