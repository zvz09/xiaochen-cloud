package com.zvz09.xiaochen.data.desensitize.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Desensitizes {
    public static final String REPLACER = "*";
    public static final char REPLACER_CHAR = '*';

    private Desensitizes() {
    }

    public static String mixFirstName(String s) {
        return mix(s, 0, s.length() - 1, '*');
    }

    public static String mixName(String s) {
        return mix(s, 1, 0, '*');
    }

    public static String mixPhone(String s) {
        return mix(s, 3, 4, "****");
    }

    public static String mixCardNum(String s) {
        return mix(s, 2, 2, "**************");
    }

    public static String mix(String s, int keepStart, int keepEnd) {
        return mix(s, keepStart, keepEnd, '*');
    }

    public static String mix(String s, int keepStart, int keepEnd, char replacer) {
        int length  = StringUtils.length(s);
        return length == 0 ? "" : mix(s, keepStart, keepEnd, StringUtils.repeat(replacer, length - keepStart - keepEnd), 1);
    }

    public static String mix(String s, int keepStart, int keepEnd, String replacer) {
        return mix(s, keepStart, keepEnd, replacer, 1);
    }

    public static String mix(String s, int keepStart, int keepEnd, String replacer, int repeat) {
        int length = StringUtils.length(s);
        if (length == 0) {
            return "";
        } else {
            if (keepStart < 0) {
                keepStart = 0;
            }

            if (keepEnd < 0) {
                keepEnd = 0;
            }

            if (keepStart + keepEnd >= length) {
                return s;
            } else {
                char[] chars = s.toCharArray();
                char[] replacerArr = StringUtils.repeat(replacer, repeat).toCharArray();
                char[] res = new char[keepStart + keepEnd + replacerArr.length];
                System.arraycopy(chars, 0, res, 0, keepStart);
                System.arraycopy(replacerArr, 0, res, keepStart, replacerArr.length);
                System.arraycopy(chars, chars.length - keepEnd, res, keepStart + replacerArr.length, keepEnd);
                return new String(res);
            }
        }
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static <T> T def(T o, T def) {
        return isNull(o) ? def : o;
    }

    public static String object2String(Object o) {
        if (o == null) {
            return "";
        } else if (o instanceof String) {
            return (String)o;
        } else if (o instanceof Object[]) {
            return Arrays.toString((Object[])((Object[])o));
        } else if (o instanceof byte[]) {
            return Arrays.toString((byte[])((byte[])o));
        } else if (o instanceof short[]) {
            return Arrays.toString((short[])((short[])o));
        } else if (o instanceof int[]) {
            return Arrays.toString((int[])((int[])o));
        } else if (o instanceof long[]) {
            return Arrays.toString((long[])((long[])o));
        } else if (o instanceof float[]) {
            return Arrays.toString((float[])((float[])o));
        } else if (o instanceof double[]) {
            return Arrays.toString((double[])((double[])o));
        } else if (o instanceof char[]) {
            return Arrays.toString((char[])((char[])o));
        } else {
            return o instanceof boolean[] ? Arrays.toString((boolean[])((boolean[])o)) : (String)def(o.toString(), (Object)"");
        }
    }

}
