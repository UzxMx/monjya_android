package com.monjya.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.monjya.android.R;

/**
 * Created by xmx on 2017/2/12.
 */

public class CustomTabView extends FrameLayout {

    private static final String TAG = "CustomTabView";

    public ImageView imageView;

    public TextView textView;

    public CustomTabView(Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.view_tab, this, false);
        addView(view);

        imageView = (ImageView) view.findViewById(R.id.image_view);
        textView = (TextView) view.findViewById(R.id.text_view);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if (imageView != null) {
            imageView.setSelected(selected);
        }

        if (textView != null) {
            textView.setSelected(selected);
        }
    }
}
