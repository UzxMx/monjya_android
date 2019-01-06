package com.monjya.android.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xmx on 2016/12/28.
 */

public class StreamUtils {

    public static String readInputStreamAndClose(InputStream inputStream) {
        try {
            if (inputStream == null) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            char[] buf = new char[1024];
            StringBuilder builder = new StringBuilder();
            int count;
            while ((count = reader.read(buf)) != -1) {
                builder.append(buf, 0, count);
            }
            return builder.toString();
        } catch (Throwable throwable) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
