package com.monjya.android.order;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.monjya.android.app.Monjya;
import com.monjya.android.net.FormStringRequest;
import com.monjya.android.net.JsonRequest;
import com.monjya.android.net.RequestManager;
import com.monjya.android.net.URL;
import com.monjya.android.util.JSONUtils;
import com.monjya.android.util.LogManager;
import com.monjya.android.util.URLUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xmx on 2017/3/12.
 */

public class OrdersManager {

    private static final String TAG = "OrdersManager";

    private static OrdersManager singleton;

    private List<WeakReference<Listener>> listeners = new ArrayList<>();

    private OrdersManager() {
    }

    public static OrdersManager getInstance() {
        if (singleton == null) {
            synchronized (OrdersManager.class) {
                if (singleton == null) {
                    singleton = new OrdersManager();
                }
            }
        }
        return singleton;
    }

    public interface PostOrderCallback {
        void onSuccess(Order order);
        void onError(VolleyError error);
    }

    public void postOrder(Long productId, String startDate, String startPlace, List<Long> visitorIds, PostOrderCallback callback) {
        final WeakReference<PostOrderCallback> callbackWeakReference = new WeakReference<PostOrderCallback>(callback);
        JSONObject jsonObject = JSONUtils.generateJSONObject("product_id", productId, "start_date", startDate, "start_place", startPlace, "visitor_ids", new JSONArray(visitorIds));
        JsonRequest request = new JsonRequest(Request.Method.POST, URL.URL_ORDERS, jsonObject.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Order order = JSONUtils.toObject(response, Order.class);
                PostOrderCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess(order);
                }

                onOrdersChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PostOrderCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        RequestManager.getInstance().addRequestWithAuth(request);
    }

    public interface GetOrdersCallback {
        void onSuccess(List<Order> orders, boolean hasMore);
        void onError(VolleyError error);
    }

    public void getOrders(int offset, boolean ongoing, GetOrdersCallback callback) {
        final WeakReference<GetOrdersCallback> callbackWeakReference = new WeakReference<GetOrdersCallback>(callback);
        Map<String, String> map = new HashMap<>();
        map.put("page", Integer.toString(Monjya.calculatePage(offset)));
        map.put("per", Integer.toString(Monjya.PAGE_SIZE));
        String url = URLUtils.addQueryParams(URL.URL_ORDERS, map);
        FormStringRequest request = new FormStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (LogManager.isDebugEnabled()) {
                    LogManager.d(TAG, "getOrders resp: " + response);
                }

                List<Order> orders = JSONUtils.toList(response, Order.class);
                int size = 0;
                if (orders != null) {
                    size = orders.size();
                }
                GetOrdersCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess(orders, size == Monjya.PAGE_SIZE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GetOrdersCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        RequestManager.getInstance().addRequestWithAuth(request);
    }

    public interface GetOrderCallback {
        void onSuccess(Order order);
        void onError(VolleyError error);
    }

    public void getOrder(Long id, GetOrderCallback callback) {
        final WeakReference<GetOrderCallback> callbackWeakReference = new WeakReference<GetOrderCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.GET, URL.urlOrder(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Order order = JSONUtils.toObject(response, Order.class);
                GetOrderCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess(order);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GetOrderCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        RequestManager.getInstance().addRequestWithAuth(request);
    }

    private void onOrdersChanged() {
        Iterator<WeakReference<Listener>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<Listener> ref = iterator.next();
            Listener listener = ref.get();
            if (listener != null) {
                listener.onOrdersChanged();
            }
        }
    }

    public void registerListener(Listener listener) {
        for (int i = 0; i < listeners.size(); ++i) {
            if (listeners.get(i).get() == listener) {
                return;
            }
        }
        listeners.add(new WeakReference<Listener>(listener));
    }

    public void unregisterListener(Listener listener) {
        Iterator<WeakReference<Listener>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<Listener> ref = iterator.next();
            if (ref.get() == listener) {
                iterator.remove();
                return;
            }
        }
    }

    public interface Listener {
        void onOrdersChanged();
    }
}
