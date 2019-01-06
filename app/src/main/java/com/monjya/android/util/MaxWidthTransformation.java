package com.monjya.android.util;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

import java.io.Serializable;

/**
 * Created by xmx on 2016/12/8.
 */

public class MaxWidthTransformation implements Transformation, Serializable {

    private int maxWidth;

    public MaxWidthTransformation(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int width = source.getWidth();
        if (width <= maxWidth) {
            return source;
        }
        int targetHeight = (int) (source.getHeight() * 1.0 / width * maxWidth);
        Bitmap output = Bitmap.createScaledBitmap(source, maxWidth, targetHeight, false);
        if (output != source) {
            BitmapUtils.release(source);
        }
        return output;
    }

    @Override
    public String key() {
        return "MaxWidthTransformation" + maxWidth;
    }
}
