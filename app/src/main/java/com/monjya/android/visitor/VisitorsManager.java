package com.monjya.android.visitor;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.monjya.android.net.FormStringRequest;
import com.monjya.android.net.RequestManager;
import com.monjya.android.net.URL;
import com.monjya.android.util.JSONUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xmx on 2017/3/8.
 */

public class VisitorsManager {

    private static final String TAG = "VisitorsManager";

    private static VisitorsManager singleton;

    private VisitorsManager() {
    }

    public static VisitorsManager getInstance() {
        if (singleton == null) {
            synchronized (VisitorsManager.class) {
                if (singleton == null) {
                    singleton = new VisitorsManager();
                }
            }
        }
        return singleton;
    }

    public interface GetVisitorsCallback {
        void onSuccess(List<Visitor> visitors);
        void onError(VolleyError error);
    }

    public void getVisitors(GetVisitorsCallback callback) {
        final WeakReference<GetVisitorsCallback> callbackWeakReference = new WeakReference<GetVisitorsCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.GET, URL.URL_VISITORS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Visitor> visitors = JSONUtils.toList(response, Visitor.class);
                GetVisitorsCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess(visitors);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GetVisitorsCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        RequestManager.getInstance().addRequestWithAuth(request);
    }

    public interface PostVisitorCallback {
        void onSuccess(Visitor visitor);
        void onError(VolleyError error);
    }

    public void postVisitor(String name, String telephone, PostVisitorCallback callback) {
        final WeakReference<PostVisitorCallback> callbackWeakReference = new WeakReference<PostVisitorCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.POST, URL.URL_VISITORS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Visitor visitor = JSONUtils.toObject(response, Visitor.class);
                PostVisitorCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess(visitor);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PostVisitorCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        if (name != null) {
            request.addParam("name", name);
        }
        if (telephone != null) {
            request.addParam("telephone", telephone);
        }
        RequestManager.getInstance().addRequestWithAuth(request);
    }

    public interface PutVisitorCallback {
        void onSuccess();
        void onError(VolleyError error);
    }

    public void putVisitor(Long id, String name, String telephone, PutVisitorCallback callback) {
        final WeakReference<PutVisitorCallback> callbackWeakReference = new WeakReference<PutVisitorCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.PUT, URL.urlVisitor(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                PutVisitorCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PutVisitorCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        if (name != null) {
            request.addParam("name", name);
        }
        if (telephone != null) {
            request.addParam("telephone", telephone);
        }
        RequestManager.getInstance().addRequestWithAuth(request);
    }

    public interface DeleteVisitorCallback {
        void onSuccess();
        void onError(VolleyError error);
    }

    public void deleteVisitor(Long id, DeleteVisitorCallback callback) {
        final WeakReference<DeleteVisitorCallback> callbackWeakReference = new WeakReference<DeleteVisitorCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.DELETE, URL.urlVisitor(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DeleteVisitorCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DeleteVisitorCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        RequestManager.getInstance().addRequestWithAuth(request);
    }
}
