package com.vincentcodes.app.consts;

public class WindowsCommandConsts {
    public static int THREAD_QUEUE_SIZE = 4096;
    public static int SCREEN_CAP_FPS = 30;

    // https://trac.ffmpeg.org/wiki/Capture/Desktop
    public static String VIDEO_CODEC = "libx264"; // "libx264rgb" for lossless

    /**
     * Higher -> reduce file size (but higher CPU load)
     * Lower  -> higher file size (but lower CPU load)
     */
    public static int COMPRESSION_RATIO = 30;

    public static final String[] CAP_SCREEN_WITH_AUDIO = {
            "{{ffmpeg_bin}}",
            "-f", "dshow",
            "-thread_queue_size", THREAD_QUEUE_SIZE + "",
            "-i", "audio=Stereo Mix (Realtek High Definition Audio)",
            "-f", "gdigrab",
            "-thread_queue_size", THREAD_QUEUE_SIZE + "",
            "-framerate", SCREEN_CAP_FPS + "",
            "-i", "desktop",
            "-c:v", VIDEO_CODEC,
            "-crf", COMPRESSION_RATIO + "",
            "-preset", "ultrafast",
            "-write_xing", "0",
            "-vf", "format=yuv420p",
            "{{filename}}"
    };

    public static final String[] CAP_SCREEN_WITHOUT_AUDIO = {
            "{{ffmpeg_bin}}",
            "-f", "gdigrab",
            "-thread_queue_size", THREAD_QUEUE_SIZE + "",
            "-framerate", SCREEN_CAP_FPS + "",
            "-i", "desktop",
            "-c:v", VIDEO_CODEC,
            "-crf", COMPRESSION_RATIO + "",
            "-preset", "ultrafast",
            "-write_xing", "0",
            "-vf", "format=yuv420p",
            "{{filename}}"
    };

    public static final String[] CAP_SCREEN_WITH_MIC = {
            "{{ffmpeg_bin}}",
            "-f", "dshow",
            "-thread_queue_size", THREAD_QUEUE_SIZE + "",
            "-i", "audio=Microphone (Realtek High Definition Audio)",
            "-f", "gdigrab",
            "-thread_queue_size", THREAD_QUEUE_SIZE + "",
            "-framerate", SCREEN_CAP_FPS + "",
            "-i", "desktop",
            "-c:v", VIDEO_CODEC,
            "-crf", COMPRESSION_RATIO + "",
            "-preset", "ultrafast",
            "-write_xing", "0",
            "-vf", "format=yuv420p",
            "{{filename}}"
    };

    public static final String[] CAP_AUDIO_ONLY = {
            "{{ffmpeg_bin}}",
            "-f", "dshow",
            "-thread_queue_size", THREAD_QUEUE_SIZE + "",
            "-i", "audio=Stereo Mix (Realtek High Definition Audio)",
            "-write_xing", "0",
            "{{filename}}"
    };

    public static final String[] CAP_MIC_ONLY = {
            "{{ffmpeg_bin}}",
            "-f", "dshow",
            "-thread_queue_size", THREAD_QUEUE_SIZE + "",
            "-i", "audio=Microphone (Realtek High Definition Audio)",
            "-write_xing", "0",
            "{{filename}}"
    };
}
