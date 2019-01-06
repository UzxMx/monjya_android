package com.monjya.android.product;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.monjya.android.app.Monjya;
import com.monjya.android.net.FormStringRequest;
import com.monjya.android.net.RequestManager;
import com.monjya.android.net.URL;
import com.monjya.android.util.JSONUtils;
import com.monjya.android.util.LogManager;
import com.monjya.android.util.URLUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xmx on 2017/2/19.
 */

public class ProductsManager {

    private static final String TAG = "ProductsManager";

    private static ProductsManager singleton;

    public static ProductsManager getInstance() {
        if (singleton == null) {
            synchronized (ProductsManager.class){
                if (singleton == null) {
                    singleton = new ProductsManager();
                }
            }
        }
        return singleton;
    }

    public interface GetProductsCallback {
        void onSuccess(List<Product> products, boolean hasMore);
        void onError(VolleyError error);
    }

    public void getProducts(int offset, GetProductsCallback callback) {
        final WeakReference<GetProductsCallback> callbackWeakReference = new WeakReference<GetProductsCallback>(callback);
        Map<String, String> map = new HashMap<>();
        map.put("page", Integer.toString(Monjya.calculatePage(offset)));
        map.put("per", Integer.toString(Monjya.PAGE_SIZE));
        String url = URLUtils.addQueryParams(URL.URL_PRODUCTS, map);
        FormStringRequest request = new FormStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (LogManager.isDebugEnabled()) {
                    LogManager.d(TAG, "getProducts resp: " + response);
                }

                List<Product> list = JSONUtils.toList(response, Product.class);
                int size = 0;
                if (list != null) {
                    size = list.size();
                }

                GetProductsCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess(list, size == Monjya.PAGE_SIZE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GetProductsCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        RequestManager.getInstance().addRequest(request);
    }

    public interface GetProductCallback {
        void onSuccess(Product product);
        void onError(VolleyError error);
    }

    public void getProduct(Long id, GetProductCallback callback) {
        final WeakReference<GetProductCallback> callbackWeakReference = new WeakReference<GetProductCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.GET, URL.urlProduct(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (LogManager.isDebugEnabled()) {
                    LogManager.d(TAG, "getProduct resp: " + response);
                }

                Product product = JSONUtils.toObject(response, Product.class);
                GetProductCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess(product);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                GetProductCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        RequestManager.getInstance().addRequest(request);
    }
}
