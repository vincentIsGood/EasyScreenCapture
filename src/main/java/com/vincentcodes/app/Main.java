package com.vincentcodes.app;

import com.vincentcodes.app.consts.WindowsCommandConsts;
import com.vincentcodes.app.utils.SimpleLogger;
import com.vincentcodes.app.win.ScreenCaptureWindows;
import com.vincentcodes.util.commandline.ArgumentObjectMapper;
import com.vincentcodes.util.commandline.ObjectMapperParseResult;

import java.io.File;

public class Main {
    private static final SimpleLogger LOGGER = new SimpleLogger(ScreenCaptureWindows.class);

    public static void main(String[] args) {
        ObjectMapperParseResult<CommandLineArgs> parseResult = ArgumentObjectMapper.parseToObject(args, CommandLineArgs.class);
        if(parseResult.result.help){
            parseResult.simplePrintHelp("java -jar EasyScreenCapture-x.x.jar [options]");
            return;
        }

        CommandLineArgs cmdArgs = parseResult.result;

        File file = new File(cmdArgs.outdir);

        if(!file.isDirectory())
            throw new IllegalArgumentException("Invalid output directory: '" + file + "'");

        WindowsCommandConsts.THREAD_QUEUE_SIZE = cmdArgs.threadQueueSize;
        WindowsCommandConsts.SCREEN_CAP_FPS = cmdArgs.screenFpsCap;
        WindowsCommandConsts.MIC_VOLUME_INCREASE = cmdArgs.micVolumeIncrease;
        WindowsCommandConsts.VIDEO_CODEC = cmdArgs.videoCodec;
        WindowsCommandConsts.COMPRESSION_RATIO = cmdArgs.compressionRatio;

        printKeymap();
        new ScreenCaptureWindows(file);
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