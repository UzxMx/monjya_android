package com.monjya.android.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.monjya.android.R;
import com.monjya.android.account.AccountManager;
import com.monjya.android.app.Monjya;
import com.monjya.android.util.StringUtils;
import com.monjya.android.util.ToastManager;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class RequestManager {

    public static final String HEADER_AUTHORIZATION = "Authorization";

    private static RequestManager singleton;

    private RequestQueue requestQueue;

    private RequestManager() {
        requestQueue = Volley.newRequestQueue(Monjya.getInstance().getApplicationContext());
    }

    public static RequestManager getInstance() {
        if (singleton == null) {
            synchronized (RequestManager.class) {
                if (singleton == null) {
                    singleton = new RequestManager();
                }
            }
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }

    public void addRequest(Request request) {
        requestQueue.add(request);
    }

    public void addRequestWithAuth(AbstractRequest request) {
        String token = AccountManager.getInstance().getToken();
        if (!StringUtils.isBlank(token)) {
            request.addHeader(HEADER_AUTHORIZATION, token);
        }

        addRequest(request);
    }

    public static void showError(VolleyError volleyError) {
        Monjya.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                ToastManager.showToast(R.string.network_error);
            }
        });
    }
}
