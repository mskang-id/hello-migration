package com.shopmall.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat TS  = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String today() {
        return SDF.format(new Date());
    }

    public static String now() {
        return TS.format(new Date());
    }

    public static boolean isExpired(String yyyymmdd) {
        return yyyymmdd.compareTo(today()) < 0;
    }
}
