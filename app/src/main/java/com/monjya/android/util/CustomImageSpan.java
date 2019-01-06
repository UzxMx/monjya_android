package com.monjya.android.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by xmx on 2016/11/23.
 */

public class CustomImageSpan extends ImageSpan {

    private static final String TAG = "CustomImageSpan";

    public CustomImageSpan(Drawable d) {
        super(d);
    }

    public CustomImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return super.getSize(paint, text, start, end, fm) + 10;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        canvas.translate(10, 3);
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);

//        paint.setColor(Color.RED);
//        canvas.drawRect(x, top, y, bottom, paint);

//        LogManager.d(TAG, x + " " + top + " " + y + " " + bottom);
    }
}
