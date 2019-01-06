package com.monjya.android.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by xmx on 2017/2/17.
 */

public class StoppableWebView extends WebView {

    private OnOverScrollTopListener onOverScrollTopListener;

    private PointF lastPoint;

    public StoppableWebView(Context context) {
        super(context);
    }

    public StoppableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoppableWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAtTopEnd()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public boolean isAtTopEnd() {
        return getScrollY() == 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (lastPoint == null && isAtTopEnd()) {
                    lastPoint = new PointF(event.getX(), event.getY());
                } else if (lastPoint != null) {
                    if (event.getY() > lastPoint.y) {
                        lastPoint = null;
                        if (onOverScrollTopListener != null) {
                            onOverScrollTopListener.onOverScroll();
                        }
                        return false;
                    } else {
                        lastPoint = null;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnOverScrollTopListener(OnOverScrollTopListener onOverScrollTopListener) {
        this.onOverScrollTopListener = onOverScrollTopListener;
    }

    public interface OnOverScrollTopListener {
        void onOverScroll();
    }
}
