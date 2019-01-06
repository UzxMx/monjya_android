package com.monjya.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.monjya.android.R;
import com.monjya.android.util.ScreenUtils;

import java.lang.ref.WeakReference;

/**
 * Created by xuemingxiang on 16-11-14.
 */

public abstract class BaseDialog {

    protected Dialog dialog;

    private WeakReference<Context> contextWeakReference;

    public BaseDialog(Context context, int resId) {
        this(context, resId, R.style.CustomDialogStyle);
    }

    public BaseDialog(Context context, int resId, int themeId) {
        contextWeakReference = new WeakReference<Context>(context);
        dialog = new Dialog(context, themeId);
        View contentView = LayoutInflater.from(context).inflate(resId, null, false);
        dialog.setContentView(contentView);
        dialog.setCancelable(true);

        initContentView(context, contentView);
    }

    protected abstract void initContentView(Context context, View contentView);

    public void show() {
        try {
            if (dialog != null) {
                dialog.show();
            }
        } catch (Throwable throwable) {
        }
    }

    public void cancel() {
        try {
            if (dialog != null) {
                dialog.cancel();
            }
        } catch (Throwable throwable) {
        }
    }

    public void setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        dialog.setOnCancelListener(onCancelListener);
    }

    public void setWidth(int width) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width;
        window.setAttributes(lp);
    }

    public Context getContext() {
        return contextWeakReference == null ? null : contextWeakReference.get();
    }

    public void setWidth(float ratio) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (ScreenUtils.getScreenWidth(getContext()) * ratio);
        window.setAttributes(lp);
    }
}
