package com.vincentcodes.app.win;

import com.vincentcodes.app.CmdProcessRunner;
import com.vincentcodes.app.FFmpegInstaller;
import com.vincentcodes.app.consts.KeyMap;
import com.vincentcodes.app.consts.WindowsCommandConsts;
import com.vincentcodes.app.utils.SimpleLogger;
import com.vincentcodes.app.utils.TemplateUtils;
import com.vincentcodes.app.utils.TimeNow;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScreenCaptureWindows implements Closeable {
    private static final ExecutorService LISTENER_EXECUTOR = Executors.newFixedThreadPool(1);
    private static final ExecutorService FFMPEG_EXECUTOR = Executors.newFixedThreadPool(1);
    private static final SimpleLogger LOGGER = new SimpleLogger(ScreenCaptureWindows.class);

    private static final int SECONDS_FOR_SHUTDOWN = 60;

    private final File outputDir;
    private final KeyboardListenerWin listener;

    private String ffmpegExecutablePath = "ffmpeg";
    private boolean capturerEnabled = true;
    private boolean stopAndStartAnotherFFmpeg = true;

    private CmdProcessRunner ffmpegRunner;
    private boolean ffmpegStarted = false;

    public ScreenCaptureWindows(File outputDir) {
        if(!outputDir.isDirectory())
            throw new IllegalArgumentException(outputDir.toString() + " is not a directory");
        this.outputDir = outputDir;

        if(!FFmpegInstaller.isFFmpegInstalled()) {
            FFmpegInstaller ffmpegInstaller = new FFmpegInstaller();
            ffmpegInstaller.install();
            try {
                ffmpegExecutablePath = ffmpegInstaller.getFfmpegExecutable().getCanonicalPath();
            } catch (IOException e) {
                LOGGER.error("Cannot get ffmpeg path location");
            }
        }

        listener = setupKeyListener();

        LOGGER.info("Starting key listener");
        LISTENER_EXECUTOR.submit(listener);
    }
    public ScreenCaptureWindows(String outputDir) {
        this(new File(outputDir));
    }
    public ScreenCaptureWindows() {
        this(new File("./"));
    }

    private KeyboardListenerWin setupKeyListener(){
        return new KeyboardListenerWin((key, ctrl, shift) -> {
            if(KeyMap.IS_KILL_PROGRAM.test(key, ctrl, shift)){
                LOGGER.info("Killing Easy Screen Capture");
                System.exit(0);
            }else if(KeyMap.IS_TOGGLE_PROGRAM.test(key, ctrl, shift)){
                capturerEnabled = !capturerEnabled;
                LOGGER.info("Easy Screen Capturer is " + (capturerEnabled? "enabled" : "disabled"));
            }

            if(!capturerEnabled)
                return;

            if(ffmpegStarted) {
                try {
                    if(KeyMap.IS_TERM_FFMPEG.test(key, ctrl, shift)) {
                        stopRunningFFmpeg();
                    }else if(KeyMap.IS_KILL_FFMPEG.test(key, ctrl, shift))
                        killRunningFFmpeg();
                } catch (IOException | InterruptedException e) {
                    LOGGER.error("Cannot stop / kill ffmpeg: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            if(!ffmpegStarted || stopAndStartAnotherFFmpeg){
                try {
                    if (KeyMap.CAP_SCREEN_WITH_AUDIO.test(key, ctrl, shift)) {
                        if (stopAndStartAnotherFFmpeg) stopRunningFFmpeg();
                        recordScreenWithAudio();
                    } else if (KeyMap.CAP_SCREEN_ONLY.test(key, ctrl, shift)) {
                        if (stopAndStartAnotherFFmpeg) stopRunningFFmpeg();
                        recordScreenOnly();
                    } else if (KeyMap.CAP_SCREEN_WITH_MIC.test(key, ctrl, shift)) {
                        if (stopAndStartAnotherFFmpeg) stopRunningFFmpeg();
                        recordScreenWithMic();
                    } else if (KeyMap.CAP_AUDIO_ONLY.test(key, ctrl, shift)) {
                        if (stopAndStartAnotherFFmpeg) stopRunningFFmpeg();
                        recordAudioOnly();
                    } else if (KeyMap.CAP_MIC_ONLY.test(key, ctrl, shift)) {
                        if (stopAndStartAnotherFFmpeg) stopRunningFFmpeg();
                        recordMicOnly();
                    }
                }catch (IOException | InterruptedException e){
                    LOGGER.error("Cannot stop / kill ffmpeg: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void recordScreenWithAudio() {
        LOGGER.info("Record screen with desktop audio");
        runCommand(TemplateUtils.injectTemplate(
                WindowsCommandConsts.CAP_SCREEN_WITH_AUDIO(),
                Map.of("filename", getOutputFilename("mp4"),
                        "ffmpeg_bin", ffmpegExecutablePath)
        ));
    }
    public void recordScreenOnly() {
        LOGGER.info("Record screen only");
        runCommand(TemplateUtils.injectTemplate(
                WindowsCommandConsts.CAP_SCREEN_WITHOUT_AUDIO(),
                Map.of("filename", getOutputFilename("mp4"),
                        "ffmpeg_bin", ffmpegExecutablePath)
        ));
    }
    public void recordScreenWithMic() {
        LOGGER.info("Record screen with microphone");
        runCommand(TemplateUtils.injectTemplate(
                WindowsCommandConsts.CAP_SCREEN_WITH_MIC(),
                Map.of("filename", getOutputFilename("mp4"),
                        "ffmpeg_bin", ffmpegExecutablePath)
        ));
    }
    public void recordAudioOnly() {
        LOGGER.info("Record desktop audio only");
        runCommand(TemplateUtils.injectTemplate(
                WindowsCommandConsts.CAP_AUDIO_ONLY(),
                Map.of("filename", getOutputFilename("mp3"),
                        "ffmpeg_bin", ffmpegExecutablePath)
        ));
    }
    public void recordMicOnly() {
        LOGGER.info("Record microphone only");
        runCommand(TemplateUtils.injectTemplate(
                WindowsCommandConsts.CAP_MIC_ONLY(),
                Map.of("filename", getOutputFilename("mp3"),
                        "ffmpeg_bin", ffmpegExecutablePath)
        ));
    }

    private void stopRunningFFmpeg() throws IOException, InterruptedException {
        if(ffmpegRunner == null) return;
        Process proc = ffmpegRunner.getRunningProc();
        if(!proc.isAlive()) return;

        LOGGER.info("\nTrying to stop ffmpeg");
        proc.getOutputStream().write('q');
        proc.getOutputStream().flush();
        LOGGER.info("\nWait "+ SECONDS_FOR_SHUTDOWN +"s for it to shutdown");
        proc.waitFor(SECONDS_FOR_SHUTDOWN, TimeUnit.SECONDS);
        if(!proc.isAlive()){
            ffmpegStarted = false;
            LOGGER.info("ffmpeg stopped");
        }else LOGGER.warn("ffmpeg is still running");
    }
    private void killRunningFFmpeg() throws InterruptedException {
        if (ffmpegRunner == null) return;
        Process proc = ffmpegRunner.getRunningProc();
        if(!proc.isAlive()) return;

        LOGGER.info("\nTrying to kill ffmpeg");
        proc.destroy();
        LOGGER.info("\nWait "+ SECONDS_FOR_SHUTDOWN +"s for it to shutdown");
        proc.waitFor(SECONDS_FOR_SHUTDOWN, TimeUnit.SECONDS);
        if(!proc.isAlive()){
            ffmpegStarted = false;
            LOGGER.info("ffmpeg stopped");
        }else LOGGER.warn("ffmpeg is still running");
    }

        /**
         * [blocking] Runs a command with given program and
         }args
     */
    private void runCommand(String[] cmd){
        ensureSingleRunningFFmpeg();

        LOGGER.info("Starting an ffmpeg program with: " + String.join(" ", cmd));
        FFMPEG_EXECUTOR.submit(()->{
            try(final CmdProcessRunner RUNNER = new CmdProcessRunner()){
                ffmpegRunner = RUNNER;
                ffmpegStarted = true;
                LOGGER.debug("Task submitted. Running the command now");
                ffmpegRunner.runSync(cmd);
                ffmpegStarted = false;
            } catch (IOException e) {
                e.printStackTrace();

                LOGGER.error("Cannot start ffmpeg. Stopping it");
                ffmpegRunner.getRunningProc().destroy();
            }
        });
    }

    private void ensureSingleRunningFFmpeg(){
        if(ffmpegStarted) {
            throw new IllegalStateException("An existing ffmpeg job has not finished");
        }
    }

    private String getOutputFilename(String fileExt){
        String filename = "recording_" + TimeNow.string() + "." + fileExt;
        try {
            return this.outputDir.getCanonicalPath() + File.separator + filename;
        } catch (IOException e) {
            LOGGER.error("Error trying to create a new file. Putting it here instead: ./" + filename);
            return filename;
        }
    }

    @Override
    public void close() throws IOException {
        listener.close();
        if(ffmpegStarted && ffmpegRunner != null){
            ffmpegRunner.getRunningProc().destroy();
        }
    }
}
