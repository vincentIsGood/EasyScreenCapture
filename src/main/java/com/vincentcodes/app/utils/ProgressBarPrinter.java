package com.vincentcodes.app.utils;

public class ProgressBarPrinter {
    public static void display(int currentValue, int totalValue){
        int totalWidth = 20;
        float ratio = (currentValue*1.0f)/(totalValue*1.0f);
        int percentage = (int)Math.ceil(ratio * 100);
        int currentWidth = (int)Math.ceil(ratio*totalWidth);
        System.out.print("\r["+ progressString(currentWidth, totalWidth) +"] " + percentage + "%");
    }

    private static String progressString(int currentWidth, int totalWidth){
        StringBuilder resultString = new StringBuilder();
        for(int i = 0; i < currentWidth; i++)
            resultString.append('=');
        for(int i = currentWidth; i < totalWidth; i++)
            resultString.append(' ');
        return resultString.toString();
    }
}
