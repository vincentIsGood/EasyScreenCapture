package com.vincentcodes.app;

import com.sun.jna.Platform;
import com.vincentcodes.app.utils.ProgressBarPrinter;
import com.vincentcodes.app.utils.SimpleLogger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FFmpegInstaller {
    private static final SimpleLogger LOGGER = new SimpleLogger(FFmpegInstaller.class);

    private final File outDir;
    private final File zipFile;

    private File ffmpegBinDir;

    public FFmpegInstaller(String outDir) {
        this.outDir = new File(outDir);
        if(!this.outDir.exists())
            createDirectoryUnchecked(this.outDir);
        this.zipFile = new File(outDir, "ffmpeg.zip");
    }
    public FFmpegInstaller() {
        this("./ffmpeg_download_loc");
    }

    public void install(){
        if(!this.zipFile.exists())
            download();
        else LOGGER.info("ffmpeg.zip found");
        unzip();
        try {
            LOGGER.debug("ffmpeg bin directory is: " + ffmpegBinDir.getCanonicalPath());
        } catch (IOException e) {
            LOGGER.error("Cannot get ffmpeg bin directory: " + e.getMessage());
        }
    }

    public File getFfmpegBinDir() {
        return ffmpegBinDir;
    }
    public File getFfmpegExecutable() {
        return new File(ffmpegBinDir, Platform.isWindows()? "ffmpeg.exe" : "ffmpeg");
    }
    public File getFfplayExecutable() {
        return new File(ffmpegBinDir, Platform.isWindows()? "ffplay.exe" : "ffplay");
    }

    private void download(){
        String ffmpegUrl = "https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip";
        try {
            LOGGER.info("Downloading ffmpeg from " + ffmpegUrl);
            URL url = new URL(ffmpegUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK
            && connection.getContentLength() > 0){
                try(FileOutputStream fos = new FileOutputStream(this.zipFile)) {
                    InputStream is = connection.getInputStream();
                    int contentLength = connection.getContentLength();
                    int len, totalRead = 0;
                    byte[] buffer = new byte[2048];
                    while ((len = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                        totalRead += len;
                        ProgressBarPrinter.display(totalRead, contentLength);
                    }
                }
                return;
            }
            throw new IOException("Server responded with: " + connection.getResponseCode());
        } catch (IOException e) {
            LOGGER.error("Cannot download ffmpeg: " + e.getMessage());
        }
    }

    private void unzip(){
        try {
            LOGGER.info("Unzipping ffmpeg.zip");
            byte[] buffer = new byte[2048];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(this.zipFile));
            for (ZipEntry zipEntry = zis.getNextEntry(); zipEntry != null; zipEntry = zis.getNextEntry()) {
                File file = extractedFileLocation(outDir, zipEntry);
                if(file.getName().equals("bin"))
                    ffmpegBinDir = file;
                if(file.exists()) continue;
                if (zipEntry.isDirectory()) {
                    createDirectory(file);
                } else {
                    File parent = file.getParentFile();
                    createDirectory(parent);

                    try(FileOutputStream fos = new FileOutputStream(file)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Cannot unzip ffmpeg: " + e.getMessage());
        }
    }

    private static File extractedFileLocation(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private void createDirectoryUnchecked(File file) {
        if (!file.isDirectory() && !file.mkdirs()) {
            throw new UncheckedIOException(new IOException("Failed to create directory " + file));
        }
    }
    private void createDirectory(File file) throws IOException {
        if (!file.isDirectory() && !file.mkdirs())
            throw new IOException("Failed to create directory " + file);
    }

    public static boolean isFFmpegInstalled(){
        try {
            CmdProcessRunner runner = new CmdProcessRunner();
            runner.runSyncSilent(Platform.isWindows()? "where" : "which", "ffmpeg");
            return runner.getRunningProc().exitValue() == 0;
        } catch (IOException e) {
            LOGGER.error("Error encountered while finding whether ffmpeg is in path");
        }
        return false;
    }
}
