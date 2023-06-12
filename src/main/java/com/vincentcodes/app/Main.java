package com.vincentcodes.app;

import com.vincentcodes.app.utils.SimpleLogger;
import com.vincentcodes.app.win.ScreenCaptureWindows;

public class Main {
    private static final SimpleLogger LOGGER = new SimpleLogger(ScreenCaptureWindows.class);

    public static void main(String[] args) throws Exception {
        printKeymap();

        //TODO: add command line args
        new ScreenCaptureWindows();
    }

    private static void printKeymap(){
        LOGGER.info("Key map for Easy Screen Capture:");
        System.out.println("");
        System.out.println("Program:");
        System.out.println("CTRL + SHIFT + : -> kill program");
        System.out.println("CTRL + SHIFT + ` -> toggle program (enable / disable)");
        System.out.println("");
        System.out.println("Capture:");
        System.out.println("CTRL + SHIFT + 1 -> Cap screen with desktop audio");
        System.out.println("CTRL + SHIFT + 2 -> Cap screen only");
        System.out.println("CTRL + SHIFT + 3 -> Cap screen with microphone");
        System.out.println("CTRL + SHIFT + 4 -> Cap desktop audio only");
        System.out.println("CTRL + SHIFT + 5 -> Cap microphone audio only");
        System.out.println("");
        System.out.println("Control:");
        System.out.println("CTRL + SHIFT + Q -> End recording");
        System.out.println("CTRL + SHIFT + K -> Kill ffmpeg recorder");
    }
}