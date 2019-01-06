package com.monjya.android.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.monjya.android.R;

/**
 * Created by xmx on 2016/11/29.
 */

public class TwoButtonsDialog extends BaseDialog {

    private TextView tvContent;

    private TextView btnLeft;

    private TextView btnRight;

    public TwoButtonsDialog(Context context) {
        super(context, R.layout.dialog_two_buttons, R.style.CustomTransparentDialogStyle);
    }

    @Override
    protected void initContentView(Context context, View contentView) {
        tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        btnLeft = (TextView) contentView.findViewById(R.id.btn_left);
        btnRight = (TextView) contentView.findViewById(R.id.btn_right);
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public TextView getBtnLeft() {
        return btnLeft;
    }

    public TextView getBtnRight() {
        return btnRight;
    }
}
