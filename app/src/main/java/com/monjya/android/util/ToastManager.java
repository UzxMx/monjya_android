package com.monjya.android.util;

import android.content.Context;
import android.widget.Toast;

import com.monjya.android.app.Monjya;

import java.lang.ref.WeakReference;

/**
 * Created by xuemingxiang on 16-11-13.
 */

public class ToastManager {

    private static WeakReference<Toast> toastWeakReference;

    public static void showToast(Context context, String text, Integer gravity, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        showToast(toast, gravity);
    }

    private static void showToast(Toast toast, Integer gravity) {
        if (gravity != null) {
            toast.setGravity(gravity, 0, 0);
        }

        cancelToast();

        toast.show();
        toastWeakReference = new WeakReference<Toast>(toast);
    }

    public static void showToast(Context context, int stringRes, Integer gravity, int duration) {
        Toast toast = Toast.makeText(context, stringRes, duration);
        showToast(toast, gravity);
    }

    public static void showToast(int strRes) {
        Context context = Monjya.getInstance().getApplicationContext();
        if (context != null) {
            showToast(context, strRes, null, Toast.LENGTH_SHORT);
        }
    }

    public static void showToast(String text) {
        Context context = Monjya.getInstance().getApplicationContext();
        if (context != null) {
            showToast(context, text, null, Toast.LENGTH_SHORT);
        }
    }

    public static void cancelToast() {
        if (toastWeakReference != null && toastWeakReference.get() != null) {
            try {
                toastWeakReference.get().cancel();
            } catch (Throwable throwable) {
            }
        }
        toastWeakReference = null;
    }
}
