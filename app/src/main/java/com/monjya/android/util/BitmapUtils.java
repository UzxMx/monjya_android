package com.monjya.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by xmx on 2016/11/29.
 */

public class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    public static void release(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static Bitmap resizeBitmap(Context context, Uri uri, int maxWidth, int maxHeight) {
        if (context == null) {
            return null;
        }
        InputStream inputStream;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Bitmap cropped = resize(bitmap, maxWidth, maxHeight);
            release(bitmap);
            return cropped;
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static byte[] toByteArray(Bitmap bitmap, int quality) {
        if (bitmap == null) {
            return new byte[0];
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            return outputStream.toByteArray();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static byte[] toByteArray(Bitmap bitmap) {
        return toByteArray(bitmap, 80);
    }

    public static File save(Bitmap bitmap, String filename) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (LogManager.isDebugEnabled()) {
            LogManager.d(TAG, "dir: " + dir.getAbsolutePath());
        }

        File file = new File(dir, filename);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            return file;
        } catch (FileNotFoundException e) {
            LogManager.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static File save(Bitmap bitmap) {
        return save(bitmap, UUID.randomUUID().toString() + ".jpg");
    }
}
