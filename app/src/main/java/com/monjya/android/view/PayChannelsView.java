package com.monjya.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monjya.android.R;

/**
 * Created by xmx on 2017/3/12.
 */

public class PayChannelsView extends LinearLayout {

    private static int[] CHANNELS = {R.string.pay_by_alipay};

    public PayChannelsView(Context context) {
        this(context, null);
    }

    public PayChannelsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayChannelsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < CHANNELS.length; ++i) {
            int channel = CHANNELS[i];
            View view = layoutInflater.inflate(R.layout.item_pay_channel, this, false);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            tvName.setText(channel);

            if (i == CHANNELS.length - 1) {
                view.setBackgroundResource(R.drawable.layer_container_top_bottom_border);
            } else {
                view.setBackgroundResource(R.drawable.layer_container_top_border);
            }

            addView(view);
        }
    }
}
