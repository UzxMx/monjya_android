package com.monjya.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.monjya.android.R;
import com.monjya.android.util.ScreenUtils;
import com.monjya.android.util.StringUtils;
import com.monjya.android.util.UnitUtils;

import java.lang.ref.WeakReference;

/**
 * Created by xmx on 2017/2/12.
 */

public class ProgressDialog {

    private WeakReference<Context> context;

    private AlertDialog dialog;

    private AlertDialog.Builder builder;

    private boolean showOverlay = false;

    private Options options;

    public ProgressDialog(Context context) {
        this(context, null);
    }

    public ProgressDialog(Context context, Options options) {
        this.context = new WeakReference<>(context);
        this.builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        this.options = options;
    }

    @SuppressWarnings("unused")
    public void setCancellable(boolean flag) {
        builder.setCancelable(flag);
    }

    @SuppressWarnings("unused")
    public void setShowOverlay(boolean flag) {
        this.showOverlay = flag;
    }

    public void show() {
        dialog = builder.show();

        Window window = dialog.getWindow();

        Context c = context.get();
        if (c == null) {
            return;
        }
        View rootView = LayoutInflater.from(c).inflate(R.layout.dialog_progress, null);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
        Animation anim = AnimationUtils.loadAnimation(context.get(), R.anim.anim_progress_rotate);
        imageView.setAnimation(anim);

        TextView textView = (TextView) rootView.findViewById(R.id.text_view);
        if (options != null && options.showText && !StringUtils.isBlank(options.text)) {
            textView.setText(options.text);
            textView.setGravity(options.textGravity);
        } else {
//            rootView.setBackgroundColor(0);
            textView.setVisibility(View.GONE);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = UnitUtils.convertDpToPixel(c, 60);
            window.setAttributes(layoutParams);
        }

        window.setContentView(rootView);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (!showOverlay) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        if (options != null && options.widthRatio >= 0 && options.widthRatio <= 1) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = (int) (ScreenUtils.getScreenWidth(c) * options.widthRatio);
            window.setAttributes(layoutParams);
        }
    }

    public void cancel() {
        if (dialog != null) {
            dialog.hide();
            dialog.cancel();
        }
    }

    protected AlertDialog.Builder getDialogBuilder() {
        return this.builder;
    }

    public static class Options {
        public boolean showText = false;
        public String text;
        public int textGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        public float widthRatio = -1;
    }
}
