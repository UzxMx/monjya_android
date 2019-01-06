package com.monjya.android.util;

import android.widget.EditText;

/**
 * Created by xmx on 2017/3/7.
 */

public class EditTextUtils {

    public static void setText(EditText editText, String text) {
        editText.setText(text);
        editText.setSelection(text == null ? 0 : text.length());
    }
}
