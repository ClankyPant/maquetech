package com.maquetech.application.helper;

public class ConvertHelper {

    private ConvertHelper() {}

    public static String getString(Object val) {
        return val.toString();
    }

    public static String getString(Object val, String defaultValue) {
        try {
            if (val == null) return defaultValue;
            if (val.toString().equals("")) return defaultValue;
            return getString(val);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Long getLong(Object val) {
        return Long.valueOf(val.toString());
    }

    public static Long getLong(Object val, Long defaultValue) {
        try {
            if (val == null) return defaultValue;
            if (val.toString().equals("")) return defaultValue;
            return getLong(val);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Double getDouble(Object val) {
        return Double.valueOf(val.toString());
    }

    public static Double getDouble(Object val, Double defaultValue) {
        try {
            if (val == null) return defaultValue;
            if (val.toString().equals("")) return defaultValue;
            return getDouble(val);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer getInteger(Object val) {
        return Integer.valueOf(val.toString());
    }

    public static Integer getInteger(Object val, Integer defaultValue) {
        try {
            if (val == null) return defaultValue;
            if (val.toString().equals("")) return defaultValue;
            return getInteger(val);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Boolean getBoolean(Object val) {
        return Boolean.valueOf(val.toString());
    }

    public static Boolean getBoolean(Object val, Boolean defaultValue) {
        try {
            if (val == null) return defaultValue;
            if (val.toString().equals("")) return defaultValue;
            return getBoolean(val);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}