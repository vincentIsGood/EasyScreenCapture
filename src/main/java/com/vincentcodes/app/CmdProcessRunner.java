package com.vincentcodes.app;

import com.sun.jna.Platform;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * Create a command runner. Output of the process will be sent to the
 * current Java program. However, input of the current Java program will
 * not be sent to the command process.
 */
public class CmdProcessRunner implements Closeable {
    private final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(1);

    private final ProcessBuilder builder = new ProcessBuilder();
    private final BiConsumer<InputStream, InputStream> handler;

    private Process runningProc;

    public CmdProcessRunner(BiConsumer<InputStream, InputStream> handler) {
        this.handler = handler;
    }
    public CmdProcessRunner() {
        this.handler = null;
    }

    private Future<?> run(boolean doWait, boolean doInheritIO, String... args) throws IOException, IllegalStateException {
        if(runningProc != null) throw new IllegalStateException("You can only run one process.");

        if(doInheritIO) builder.inheritIO();
        builder.redirectInput(ProcessBuilder.Redirect.PIPE);
        runningProc = builder.command(args).start();
        Future<?> result = EXECUTOR_SERVICE.submit(new ProcessOutputHandler(
                runningProc.getInputStream(), runningProc.getErrorStream(), handler));
        try {
            if(doWait)
                runningProc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Future<?> run(String... args) throws IOException {
        return run(false, true, args);
    }
    public Future<?> runSync(String... args) throws IOException {
        return run(true, true, args);
    }
    public Future<?> runSyncSilent(String... args) throws IOException {
        return run(true, false, args);
    }
    public Process getRunningProc(){
        return runningProc;
    }

    @Override
    public void close() throws IOException {
        runningProc.destroyForcibly();

        EXECUTOR_SERVICE.shutdown();
        try{
            if(!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)){
                EXECUTOR_SERVICE.shutdownNow();
                if(!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)){
                    System.err.println("Executor won't stop");
                }
            }
        }catch (InterruptedException e){
            EXECUTOR_SERVICE.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
