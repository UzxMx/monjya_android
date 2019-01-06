package com.monjya.android.util;

/**
 * Created by xmx on 2016/11/25.
 */

public class NumberUtils {

    public static String convertNumberToChinese(int num) {
        String str = Integer.toString(num);
        str = str.length() > 8 ? str.substring(0, 8) : str;
        while(str.startsWith("0")) {
            str = str.substring(1);
        }
        return convertToRightFormat(str);
    }

    private static String convertToRightFormat(String str) {
        StringBuffer result = new StringBuffer();
        int length = str.length();
        boolean bZero = false;
        char[] digits = new char[] { '\0', '十', '百', '千', '万', '十', '百', '千' };
        char[] chs = new char[] { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };
        for (int i = 0; i < length; i++) {
            char ch = chs[Integer.parseInt(str.charAt(i) + "")];
            char digit = digits[length - 1 - i];
            // 处理零和连续零的情况
            if (ch == '零') {
                bZero = true;
                if (digit == '万') {
                    result.append('万');
                }
                continue;
            }
            if (bZero) {
                result.append('零');
                bZero = false;
            }
            // 处理一十的情况
            if (digit == '十' && ch == '一') {
                result.append("十");
                continue;
            }
            result.append(ch).append(digit);
        }
        return result.toString();
    }
}
