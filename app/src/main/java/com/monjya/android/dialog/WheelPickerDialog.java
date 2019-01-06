package com.monjya.android.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.monjya.android.R;
import com.monjya.android.util.ScreenUtils;

/**
 * Created by xuemingxiang on 16-11-14.
 */

public abstract class WheelPickerDialog extends BaseDialog {

    protected WheelPicker wheelPicker;

    private TextView tvCancel;

    private TextView tvConfirm;

    public WheelPickerDialog(Context context, int res) {
        super(context, res, R.style.DialogSlideAnim);
    }

    @Override
    protected void initContentView(Context context, View contentView) {
        preInit(context, contentView);
        init(context, contentView);
        postInit(context, contentView);
    }

    protected void preInit(Context context, View contentView) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = ScreenUtils.getScreenWidth(context);

        window.setAttributes(wlp);
    }

    protected abstract void init(Context context, View contentView);

    protected void postInit(Context context, View contentView) {
        tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
                onCancel();
            }
        });

        tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirm();
            }
        });
    }

    protected abstract void onCancel();

    protected abstract void onConfirm();
}
