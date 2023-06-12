package com.vincentcodes.app;

import java.io.InputStream;
import java.util.function.BiConsumer;

public class ProcessOutputHandler implements Runnable{
    private final InputStream stdoutStream;
    private final InputStream stderrStream;
    private final BiConsumer<InputStream, InputStream> handler;

    public ProcessOutputHandler(InputStream stdoutStream, InputStream stderrStream, BiConsumer<InputStream, InputStream> handler) {
        this.stdoutStream = stdoutStream;
        this.stderrStream = stderrStream;
        this.handler = handler;
    }

    @Override
    public void run() {
        if(handler != null)
            handler.accept(stdoutStream, stderrStream);
    }
}
