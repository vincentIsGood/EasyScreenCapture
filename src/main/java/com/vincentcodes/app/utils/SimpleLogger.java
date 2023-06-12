package com.vincentcodes.app.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SimpleLogger {
    private static final boolean PRINT_LINE_NUM = true;

    private final Class<?> clazz;
    private final String className;

    public SimpleLogger(Class<?> clazz) {
        this.clazz = clazz;
        className = Arrays.stream(clazz.getPackageName().split("\\."))
                .map(ele -> Character.toString(ele.charAt(0)))
                .collect(Collectors.joining(".")) + "." + clazz.getSimpleName();
    }

    public void info(String msg){
        println("INFO", msg);
    }
    public void warn(String msg){
        println("WARN", msg);
    }
    public void debug(String msg){
        println("DEBUG", msg);
    }
    public void error(String msg){
        printerrln("ERROR", msg);
    }

    private void println(String logLevel, String msg){
        System.out.println(formatString(logLevel, msg));
    }
    private void printerrln(String logLevel, String msg){
        System.err.println(formatString(logLevel, msg));
    }
    private String formatString(String logLevel, String msg){
        if(PRINT_LINE_NUM)
            return "["+ logLevel +"] ["+ className + ".class:" + getLineNumber() +"] " + msg;
        return "["+ logLevel +"] ["+ className + ".class] " + msg;
    }

    private int getLineNumber() {
        // skip 4 stacks (ie. the place where the person calls, eg. info(), warn(), etc.)
        return StackWalker.getInstance(StackWalker.Option.SHOW_HIDDEN_FRAMES).walk(
                (s) -> s.skip(4).findFirst()).get().getLineNumber();
    }
}
