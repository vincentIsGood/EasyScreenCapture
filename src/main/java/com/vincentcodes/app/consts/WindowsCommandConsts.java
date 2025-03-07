package com.vincentcodes.app.consts;

/**
 * @see com.vincentcodes.app.CommandLineArgs
 */
public class WindowsCommandConsts {
    public static int THREAD_QUEUE_SIZE = 4096;
    public static int SCREEN_CAP_FPS = 30;
    public static String MIC_VOLUME_INCREASE = "1.5";

    // "libx264rgb" for lossless
    // https://trac.ffmpeg.org/wiki/Capture/Desktop
    public static String VIDEO_CODEC = "libx264";

    /**
     * Higher -> reduce file size (but higher CPU load)
     * Lower  -> higher file size (but lower CPU load)
     */
    public static int COMPRESSION_RATIO = 30;

    public static String[] CAP_SCREEN_WITH_AUDIO(){
        return new String[]{
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
    }

    public static String[] CAP_SCREEN_WITHOUT_AUDIO(){
        return new String[]{
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
    }

    public static String[] CAP_SCREEN_WITH_MIC(){
        return new String[]{
                "{{ffmpeg_bin}}",
                "-f", "dshow",
                "-thread_queue_size", THREAD_QUEUE_SIZE + "",
                "-i", "audio=Microphone (Realtek High Definition Audio)",
                "-filter:a", "volume=" + MIC_VOLUME_INCREASE,
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
    }

    public static String[] CAP_AUDIO_ONLY(){
        return new String[]{
                "{{ffmpeg_bin}}",
                "-f", "dshow",
                "-thread_queue_size", THREAD_QUEUE_SIZE + "",
                "-i", "audio=Stereo Mix (Realtek High Definition Audio)",
                "-write_xing", "0",
                "{{filename}}"
        };
    }

    public static String[] CAP_MIC_ONLY(){
        return new String[]{
                "{{ffmpeg_bin}}",
                "-f", "dshow",
                "-thread_queue_size", THREAD_QUEUE_SIZE + "",
                "-i", "audio=Microphone (Realtek High Definition Audio)",
                "-filter:a", "volume=" + MIC_VOLUME_INCREASE,
                "-write_xing", "0",
                "{{filename}}"
        };
    }

    public static String[] DELAY_AUDIO_ONLY_IN_VIDEO(){
        return new String[]{
                "{{ffmpeg_bin}}",
                "-i", "{{filename}}",
                "-itsoffset", "0.6",
                "-i", "{{filename}}",
                "-acodec", "copy",
                "-vcodec", "copy",
                "-map", "0:a",
                "-map", "1:v",
                "{{outfilename}}"
        };
    }
}
