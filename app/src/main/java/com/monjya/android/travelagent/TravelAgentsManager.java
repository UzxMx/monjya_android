package com.monjya.android.travelagent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.monjya.android.net.FormStringRequest;
import com.monjya.android.net.RequestManager;
import com.monjya.android.net.URL;
import com.monjya.android.util.JSONUtils;
import com.monjya.android.util.LogManager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xmx on 2017/3/7.
 */

public class TravelAgentsManager {

    private static final String TAG = "TravelAgentsManager";

    private static TravelAgentsManager singleton;

    private TravelAgentsManager() {
    }

    public static TravelAgentsManager getInstance() {
        if (singleton == null) {
            synchronized (TravelAgentsManager.class) {
                if (singleton == null) {
                    singleton = new TravelAgentsManager();
                }
            }
        }
        return singleton;
    }

    public interface GetTravelAgentsCallback {
        void onSuccess(List<TravelAgent> travelAgents);
        void onError(VolleyError error);
    }

    public void getTravelAgents(GetTravelAgentsCallback callback) {
        final WeakReference<GetTravelAgentsCallback> callbackWeakReference = new WeakReference<GetTravelAgentsCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.GET, URL.URL_TRAVEL_AGENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (LogManager.isDebugEnabled()) {
                    LogManager.d(TAG, "getTravelAgents resp: " + response);
                }

                List<TravelAgent> travelAgents = JSONUtils.toList(response, TravelAgent.class);

                GetTravelAgentsCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess(travelAgents);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GetTravelAgentsCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });

        RequestManager.getInstance().addRequestWithAuth(request);
    }
}
