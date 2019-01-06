package com.monjya.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmx on 2016/11/21.
 */

public class CustomScrollView extends ScrollView {

    private List<OnScrollChangedListener> listeners = new ArrayList<>();

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addOnScrollChangedListener(OnScrollChangedListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (OnScrollChangedListener listener : listeners) {
            listener.onScrollChanged(this, l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(CustomScrollView scrollView, int l, int t, int oldl, int oldt);
    }

    public boolean isAtTopEnd() {
        return getScrollY() == 0;
    }

    public boolean isAtBottomEnd() {
        return getChildAt(0).getBottom() <= getHeight() + getScrollY();
    }
}
