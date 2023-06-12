package com.vincentcodes.app.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeNow {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss_SSS").withZone(ZoneId.systemDefault());

    public static String string(){
        return DATE_FORMATTER.format(Instant.now());
    }
}
