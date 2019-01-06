package com.monjya.android.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.monjya.android.activity.BaseActivity;
import com.monjya.android.util.ToastManager;

/**
 * Created by xuemingxiang on 16-11-11.
 */

public class BaseFragment extends Fragment {

    private Handler handler = new Handler();

    public void showToast(int strRes, Integer gravity, int duration) {
        ToastManager.showToast(getActivity(), strRes, gravity, duration);
    }

    public void showToast(int strRes) {
        showToast(strRes, null, Toast.LENGTH_SHORT);
    }

    public void showToast(String str, Integer gravity, int duration) {
        ToastManager.showToast(getActivity(), str, gravity, duration);
    }

    public void showToast(String str) {
        showToast(str, null, Toast.LENGTH_SHORT);
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void showProgressDialog() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.showProgressDialog();
    }

    public void hideProgressDialog() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.hideProgressDialog();
    }
}
