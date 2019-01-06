package com.monjya.android.account;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.j256.ormlite.dao.Dao;
import com.monjya.android.R;
import com.monjya.android.activity.SignInActivity;
import com.monjya.android.app.Monjya;
import com.monjya.android.db.DBHelper;
import com.monjya.android.db.UserDBHelper;
import com.monjya.android.net.FormStringRequest;
import com.monjya.android.net.RequestManager;
import com.monjya.android.net.URL;
import com.monjya.android.property.PropertyManager;
import com.monjya.android.util.JSONUtils;
import com.monjya.android.util.LogManager;
import com.monjya.android.util.StringUtils;
import com.monjya.android.util.ToastManager;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class AccountManager {

    private static final String TAG = "AccountManager";

    public static final String AUTHENTICATION_TRIGGER = "authentication_trigger";

    public static final int AUTHENTICATION_TRIGGER_BY_ACCOUNT_MANAGER = 1;

    public static final int AUTHENTICATION_TRIGGER_BY_MY_FRAGMENT = 2;

    private static AccountManager singleton;

    private Account currentAccount;

    private WeakReference<Runnable> runnableAfterAuthenticatedRef;

    private List<WeakReference<Listener>> listeners = new ArrayList<>();

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        if (singleton == null) {
            synchronized (AccountManager.class) {
                if (singleton == null) {
                    singleton = new AccountManager();
                }
            }
        }
        return singleton;
    }

    public void init() {
        loadCurrentAccount();
    }

    private void loadCurrentAccount() {
        PropertyManager propertyManager = PropertyManager.getInstance();
        String value = propertyManager.getProperty(PropertyManager.PROPERTY_CURRENT_ACCOUNT);
        if (!StringUtils.isBlank(value)) {
            if (LogManager.isDebugEnabled()) {
                LogManager.d(TAG, "user id: " + value);
            }
            Long id = Long.parseLong(value);
            this.currentAccount = getAccount(id);
            if (LogManager.isDebugEnabled()) {
                LogManager.d(TAG, this.currentAccount.getToken());
            }
        }
    }

    private void initForCurrentAccount() {
        UserDBHelper.CURRENT_USER_DATABASE_NAME = UserDBHelper.USER_DATABASE_NAME.replace(":userId",
                currentAccount.getId().toString());
    }

    /**
     *
     * @return true if user has logged in
     */
    public boolean isAuthenticated() {
        return this.currentAccount != null;
    }

    public Account getAccount(Long id) {
        DBHelper dbHelper = DBHelper.getHelper();
        Dao<Account, Long> dao = dbHelper.getAccountDao();
        try {
            Account account = dao.queryForId(id);
            return account;
        } catch (SQLException e) {
            LogManager.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public Account getCurrentAccount() {
        return this.currentAccount;
    }

    public String getToken() {
        if (this.currentAccount != null) {
            return this.currentAccount.getToken();
        }
        return null;
    }

    public interface SignInCallback {
        void onSuccess(Account account);
        void onError(VolleyError error, Integer errno);
    }

    public void signIn(String username, String password, SignInCallback callback) {
        final WeakReference<SignInCallback> callbackWeakReference = new WeakReference<SignInCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.POST, URL.URL_SIGN_IN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (LogManager.isDebugEnabled()) {
                    LogManager.d(TAG, "signIn resp: " + response);
                }

                SignInCallback callback = callbackWeakReference.get();
                JSONObject jsonObject = JSONUtils.toJSONObject(response);
                if (jsonObject != null && jsonObject.has("errno")) {
                    if (callback != null) {
                        callback.onError(null, jsonObject.optInt("errno"));
                    }
                } else {
                    Account account = JSONUtils.toObject(response, Account.class);

                    onAuthenticated(account);

                    if (callback != null) {
                        callback.onSuccess(account);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SignInCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error, null);
                }
            }
        });
        request.addParam("username", username);
        request.addParam("password", password);
        RequestManager.getInstance().addRequest(request);
    }

    private void onAuthenticated(Account account) {
        AccountDao.save(account);
        PropertyManager propertyManager = PropertyManager.getInstance();
        propertyManager.saveProperty(PropertyManager.PROPERTY_CURRENT_ACCOUNT, account.getId().toString());
        currentAccount = account;
        initForCurrentAccount();

        for (int i = 0; i < listeners.size(); ++i) {
            Listener listener = listeners.get(i).get();
            if (listener != null) {
                listener.onAuthenticated();
            }
        }
    }

    public interface SignUpCallback {
        void onSuccess(Account account);
        void onError(VolleyError error, Integer errno);
    }

    public void signUp(String username, String password, SignUpCallback callback) {
        final WeakReference<SignUpCallback> callbackWeakReference = new WeakReference<SignUpCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.POST, URL.URL_SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (LogManager.isDebugEnabled()) {
                    LogManager.d(TAG, "signUp resp: " + response);
                }

                SignUpCallback callback = callbackWeakReference.get();
                JSONObject jsonObject = JSONUtils.toJSONObject(response);
                if (jsonObject != null && jsonObject.has("errno")) {
                    if (callback != null) {
                        callback.onError(null, jsonObject.optInt("errno"));
                    }
                } else {
                    Account account = JSONUtils.toObject(response, Account.class);

                    onAuthenticated(account);

                    if (callback != null) {
                        callback.onSuccess(account);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SignUpCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error, null);
                }
            }
        });
        request.addParam("username", username);
        request.addParam("password", password);
        RequestManager.getInstance().addRequest(request);
    }

    public interface SetTravelAgentCallback {
        void onSuccess();
        void onError(VolleyError error);
    }

    public void setTravelAgent(Long travelAgentId, SetTravelAgentCallback callback) {
        final WeakReference<SetTravelAgentCallback> callbackWeakReference = new WeakReference<SetTravelAgentCallback>(callback);
        FormStringRequest request = new FormStringRequest(Request.Method.POST, URL.urlSetTravelAgent(getCurrentAccount().getId()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SetTravelAgentCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onSuccess();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SetTravelAgentCallback callback = callbackWeakReference.get();
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
        request.addParam("travel_agent_id", travelAgentId.toString());
        RequestManager.getInstance().addRequestWithAuth(request);
    }

//    public interface PutCallback {
//        void onSuccess(Account account);
//        void onError(VolleyError error);
//    }
//
//    public void putUser(final String name, PutCallback callback) {
//        final WeakReference<PutCallback> callbackWeakReference = new WeakReference<PutCallback>(callback);
//        String token = currentAccount.getToken();
//        String[] arr = token.split(":");
//        String url = URL.urlUser(arr[0]);
//        FormStringRequest request = new FormStringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                currentAccount.setName(name);
//                saveCurrentAccount();
//
//                PutCallback callback = callbackWeakReference.get();
//                if (callback != null) {
//                    callback.onSuccess(currentAccount);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                PutCallback callback = callbackWeakReference.get();
//                if (callback != null) {
//                    callback.onError(error);
//                }
//            }
//        });
//        request.addParam("name", name);
//        RequestManager.getInstance().addRequestWithAuth(request);
//    }
//
//    public void saveCurrentAccount() {
//        currentAccount.setProvidersJson(JSONUtils.toJsonString(currentAccount.getProviders()));
//        AccountDao.save(currentAccount);
//    }

    public void executeAfterAuthenticated(Context context, Runnable runnable) {
        if (isAuthenticated()) {
            runnable.run();
        } else {
            runnableAfterAuthenticatedRef = new WeakReference<Runnable>(runnable);
            login(context);
        }
    }

    public void login(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }

    public void logout() {
        PropertyManager propertyManager = PropertyManager.getInstance();
        propertyManager.saveProperty(PropertyManager.PROPERTY_CURRENT_ACCOUNT, "");

        this.currentAccount = null;

        UserDBHelper.releaseHelper();

        onLogout();
    }

    public void triggerAfterAuthenticatedRunnable() {
        if (runnableAfterAuthenticatedRef == null) {
            return;
        }
        Runnable runnable = runnableAfterAuthenticatedRef.get();
        runnableAfterAuthenticatedRef = null;
        if (runnable != null) {
            runnable.run();
        }
    }

    private void onLogout() {
        for (int i = 0; i < listeners.size(); ++i) {
            Listener listener = listeners.get(i).get();
            if (listener != null) {
                listener.onLogout();
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
        void onAuthenticated();
        void onLogout();
    }

//    public interface BindMobileCallback {
//        void onSuccess();
//        void onError(VolleyError volleyError, Integer errno);
//    }
//
//    public void bindMobile(final String mobileNumber, String code, BindMobileCallback callback) {
//        final WeakReference<BindMobileCallback> callbackWeakReference = new WeakReference<BindMobileCallback>(callback);
//        FormStringRequest request = new FormStringRequest(Request.Method.POST, URL.URL_BIND_MOBILE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (LogManager.isDebugEnabled()) {
//                    LogManager.d(TAG, "bind mobile resp: " + response);
//                }
//
//                BindMobileCallback callback = callbackWeakReference.get();
//                JSONObject jsonObject = JSONUtils.toJSONObject(response);
//                if (jsonObject != null && jsonObject.has("errno")) {
//                    if (callback != null) {
//                        callback.onError(null, jsonObject.optInt("errno", 0));
//                    }
//                } else {
//                    List<Account.Provider> providers = currentAccount.getProviders();
//                    if (providers == null) {
//                        providers = new ArrayList<>();
//                        currentAccount.setProviders(providers);
//                    }
//                    Account.Provider provider = new Account.Provider();
//                    provider.setName(Account.Provider.PROVIDER_MOBILE);
//                    provider.setId(mobileNumber);
//                    providers.add(provider);
//
//                    saveCurrentAccount();
//
//                    if (callback != null) {
//                        callback.onSuccess();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                BindMobileCallback callback = callbackWeakReference.get();
//                if (callback != null) {
//                    callback.onError(error, null);
//                }
//            }
//        });
//        request.addParam("mobile_number", mobileNumber);
//        request.addParam("code", code);
//        RequestManager.getInstance().addRequestWithAuth(request);
//    }
//
//    public static void showErrorForBindMobile(int errno) {
//        int res = -1;
//        switch (errno) {
//            case 1:
//                res = R.string.captcha_code_not_matched;
//                break;
//            case 2:
//                res = R.string.captcha_code_expired;
//                break;
//            case 3:
//                res = R.string.bind_mobile_exists;
//                break;
//            default:
//                break;
//        }
//        if (res == -1) {
//            return;
//        }
//
//        final int finalRes = res;
//        XinYa.getInstance().getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                ToastManager.showToast(finalRes);
//            }
//        });
//    }
//
//    public interface BindThirdPartyCallback {
//        void onSuccess();
//        void onError(VolleyError volleyError, Integer errno);
//    }
//
//    public void bindThirdParty(final String provider, final String uid, String name, String image, String authRaw, BindThirdPartyCallback callback) {
//        final WeakReference<BindThirdPartyCallback> callbackWeakReference = new WeakReference<BindThirdPartyCallback>(callback);
//        FormStringRequest request = new FormStringRequest(Request.Method.POST, URL.URL_BIND_WECHAT,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (LogManager.isDebugEnabled()) {
//                            LogManager.d(TAG, "resp: " + response);
//                        }
//
//                        BindThirdPartyCallback callback = callbackWeakReference.get();
//                        JSONObject jsonObject = JSONUtils.toJSONObject(response);
//                        if (jsonObject != null && jsonObject.has("errno")) {
//                            if (callback != null) {
//                                callback.onError(null, jsonObject.optInt("errno", 0));
//                            }
//                        } else {
//                            List<Account.Provider> providers = currentAccount.getProviders();
//                            if (providers == null) {
//                                providers = new ArrayList<>();
//                                currentAccount.setProviders(providers);
//                            }
//                            Account.Provider p = new Account.Provider();
//                            p.setName(provider);
//                            p.setId(uid);
//                            providers.add(p);
//
//                            saveCurrentAccount();
//
//                            if (callback != null) {
//                                callback.onSuccess();
//                            }
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                BindThirdPartyCallback callback = callbackWeakReference.get();
//                if (callback != null) {
//                    callback.onError(error, null);
//                }
//            }
//        });
//        request.addParam("provider", provider).addParam("uid", uid).addParam("name", name)
//                .addParam("image", image).addParam("auth_raw", authRaw);
//        RequestManager.getInstance().addRequestWithAuth(request);
//    }
//
//    public static void showErrorForBindThirdParty(int errno) {
//        int res = -1;
//        switch (errno) {
//            case 1:
//                res = R.string.bind_exists;
//                break;
//            default:
//                break;
//        }
//        if (res == -1) {
//            return;
//        }
//
//        final int finalRes = res;
//        XinYa.getInstance().getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                ToastManager.showToast(finalRes);
//            }
//        });
//    }

    public static void showErrorForSignIn(VolleyError volleyError, Integer errno) {
        if (volleyError != null) {
            RequestManager.showError(volleyError);
        } else if (errno != null) {
            int res = -1;
            switch (errno) {
                case 1:
                    res = R.string.account_or_password_wrong;
                    break;
                default:
                    break;
            }
            if (res == -1) {
                return;
            }

            final int finalRes = res;
            Monjya.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    ToastManager.showToast(finalRes);
                }
            });
        }
    }

    public static void showErrorForSignUp(VolleyError volleyError, Integer errno) {
        if (volleyError != null) {
            RequestManager.showError(volleyError);
        } else if (errno != null) {
            int res = -1;
            switch (errno) {
                case 1:
                    res = R.string.username_existed;
                    break;
                default:
                    break;
            }
            if (res == -1) {
                return;
            }

            final int finalRes = res;
            Monjya.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    ToastManager.showToast(finalRes);
                }
            });
        }
    }
}
