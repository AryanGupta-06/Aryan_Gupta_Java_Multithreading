package Java_Multithreading_Worksheet_1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Q1_Multi_Threaded_Logging_System {
    private final BlockingQueue<String> logQueue;
    private final AtomicBoolean running;
    private final Thread logThread;
    private final SimpleDateFormat dateFormat;

    public enum LogLevel {
        INFO, WARN, ERROR
    }

    public Q1_Multi_Threaded_Logging_System() {
        logQueue = new LinkedBlockingQueue<>();
        running = new AtomicBoolean(true);
        logThread = new Thread(this::flushLogs);
        logThread.start();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public void log(LogLevel level, String message) {
        String timestamp = dateFormat.format(new Date());
        logQueue.offer("[" + timestamp + "] [" + level + "] " + message);
    }

    private void flushLogs() {
        while (running.get() || !logQueue.isEmpty()) {
            try {
                Thread.sleep(5000);
                while (!logQueue.isEmpty()) {
                    System.out.println(logQueue.poll());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void shutdown() {
        running.set(false);
        logThread.interrupt();
        try {
            logThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Flush remaining logs
        while (!logQueue.isEmpty()) {
            System.out.println(logQueue.poll());
        }
    }

    public static void main(String[] args) {
        Q1_Multi_Threaded_Logging_System logger = new Q1_Multi_Threaded_Logging_System();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    if(j<33){
                        logger.log(LogLevel.INFO, "Thread " + Thread.currentThread().getName() + " - Log message " + j);
                    } else if (j>33 && j<67) {
                        logger.log(LogLevel.WARN, "Thread " + Thread.currentThread().getName() + " - Log message " + j);
                    } else {
                        logger.log(LogLevel.ERROR, "Thread " + Thread.currentThread().getName() + " - Log message " + j);
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(10000); // Let the logger run for a while
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.shutdown();
    }
}
