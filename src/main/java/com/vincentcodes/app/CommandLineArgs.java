package com.vincentcodes.app;

import com.vincentcodes.util.commandline.annotations.CmdOption;

public class CommandLineArgs {
    @CmdOption(shortForm = "h")
    public boolean help;

    @CmdOption(shortForm = "o", parameterDescription = "<path/to/folder>", description = "media output directory")
    public String outdir = "./";

    @CmdOption(shortForm = "t", parameterDescription = "<number>", description = "(default 4096), thread queue size for ffmpeg")
    public int threadQueueSize = 4096;

    @CmdOption(shortForm = "fps", parameterDescription = "<number>", description = "(default 30) higher the value, larger the file")
    public int screenFpsCap = 30;

    @CmdOption(value = "mic-vol-inc", parameterDescription = "<dB / multiplier>", description = "(default 1.5) (for dB increase, eg. \"10dB\") increase microphone input volume")
    public String micVolumeIncrease = "1.5";

    @CmdOption(shortForm = "vcodec", parameterDescription = "<text>", description = "(default libx264) see supported codec for ffmpeg")
    public String videoCodec = "libx264";

    @CmdOption(shortForm = "c", parameterDescription = "<number>", description = "(default 30) higher the value, smaller the size & more worse the quality")
    public int compressionRatio = 30;
}
