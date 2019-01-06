package com.monjya.android.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;

/**
 * Created by xmx on 2016/11/23.
 */

public class ViewUtils {

    public static int getTopRelative(View childView, View parentView) {
        if (childView == parentView) {
            return 0;
        }

        if (childView.getParent() == parentView) {
            return childView.getTop();
        }

        return childView.getTop() + getTopRelative((View) childView.getParent(), parentView);
    }

    public static int getLeftRelative(View childView, View parentView) {
        if (childView == parentView) {
            return 0;
        }

        if (childView.getParent() == parentView) {
            return childView.getLeft();
        }

        return childView.getLeft() + getLeftRelative((View) childView.getParent(), parentView);
    }

    public static void makeBorderRadiusButton(View view, float radius, int defaultBgColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable pressedGradientDrawable = new GradientDrawable();
        pressedGradientDrawable.setCornerRadius(radius);
        pressedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        pressedGradientDrawable.setColor(ColorUtils.darken(defaultBgColor, 0.1));
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedGradientDrawable);

        GradientDrawable defaultGradientDrawable = new GradientDrawable();
        defaultGradientDrawable.setCornerRadius(radius);
        defaultGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        defaultGradientDrawable.setColor(defaultBgColor);
        stateListDrawable.addState(new int[]{}, defaultGradientDrawable);

        setBackgroundDrawable(view, stateListDrawable);
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
