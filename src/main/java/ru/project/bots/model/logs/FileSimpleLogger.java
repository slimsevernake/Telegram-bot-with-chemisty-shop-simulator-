package ru.project.bots.model.logs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileSimpleLogger implements SimpleLogger, Runnable{

    private final Path file;
    private final ConcurrentLinkedQueue<String> logs = new ConcurrentLinkedQueue<>();

    private boolean logging = true;

    public FileSimpleLogger(String logPath) {

        file = Paths.get(logPath);

        try {
            if(!Files.exists(file))
                Files.createFile(file);
        }catch (IOException e){
            e.printStackTrace();
        }

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }


    @Override
    public void log(Exception e) {

        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace())
            sb.append(element).append("\n");

        synchronized (logs){
            logs.add(sb.toString());
            logs.notify();
        }
    }

    @Override
    public void log(String s) {

        synchronized (logs){
            logs.add(s+"\n\n");
            logs.notify();
        }

    }

    @Override
    public void run() {

        while (logging){
            try {

                synchronized (logs){
                    if (logs.isEmpty()) {
                        logs.wait();
                        continue;
                    }
                }

                StringBuilder log = new StringBuilder().append("[").append(new Date()).append("]\n");
                synchronized (logs){
                    log.append(logs.poll());
                }

                Files.write(file, log.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

            }catch (InterruptedException e){
                Thread thread = new Thread(this);
                thread.setDaemon(true);
                thread.start();
                Thread.currentThread().interrupt();
            }catch (IOException | NullPointerException e){
                e.printStackTrace();
            }
        }

    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }
}
