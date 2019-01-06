package com.squareup.picasso;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.util.Log;

import com.monjya.android.util.LogManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by xmx on 2016/12/12.
 */

public class CacheUtils {

    private static final String TAG = "CacheUtils";

    private static UrlConnectionDownloader downloader;

    private static int maxCacheSize;

    public static void initCache(Context context, int maxSize) {
        maxCacheSize = maxSize;

        context = context.getApplicationContext();
        Picasso picasso = Picasso.with(context);
        downloader = (UrlConnectionDownloader) picasso.dispatcher.downloader;
        Class<UrlConnectionDownloader> clazz = UrlConnectionDownloader.class;

        installCache(context);
    }

    public static long getCacheSize() {
        HttpResponseCache cache = (HttpResponseCache) UrlConnectionDownloader.cache;
        if (cache != null) {
            return cache.size();
        }
        return 0;
    }

    public static void clearCache(Context context) {
        HttpResponseCache cache = (HttpResponseCache) UrlConnectionDownloader.cache;
        if (cache != null) {
            try {
                cache.delete();
            } catch (IOException e) {
                LogManager.e(TAG, Log.getStackTraceString(e));
            }
            UrlConnectionDownloader.cache = null;

            installCache(context);
        }
    }

    private static void installCache(Context context) {
        File cacheDir = Utils.createDefaultCacheDir(context);
        try {
            HttpResponseCache cache = HttpResponseCache.install(cacheDir, maxCacheSize);
            UrlConnectionDownloader.cache = cache;

            if (cache == null) {
                LogManager.d(TAG, "cache is null");
            } else {
                LogManager.d(TAG, "cache size: " + cache.size() + " max size: " + cache.maxSize());
            }
        } catch (Throwable e) {
            LogManager.e(TAG, Log.getStackTraceString(e));
        }
    }
}
