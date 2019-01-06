package com.monjya.android.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xmx on 2017/2/17.
 */

public class StoppableScrollView extends CustomScrollView {

    private boolean scrollEnabled = true;

    private PointF lastPoint;

    public StoppableScrollView(Context context) {
        super(context);
    }

    public StoppableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoppableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void enableScroll() {
        this.scrollEnabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (lastPoint == null && isAtBottomEnd()) {
            lastPoint = new PointF(ev.getX(), ev.getY());
        } else if (lastPoint != null) {
            if (ev.getY() < lastPoint.y) {
                scrollEnabled = false;
                lastPoint = null;
            } else {
                lastPoint = null;
            }
        }

        if (!scrollEnabled) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}
