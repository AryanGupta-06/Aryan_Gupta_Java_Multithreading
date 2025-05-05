package Java_Multithreading_Worksheet_1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Q4_Thread_Pool_Image_Processor {

    public static void main(String[] args) {
        // Start time measurement
        long startTime = System.currentTimeMillis();

        // Create a fixed thread pool with 5 threads
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // List to hold Future objects
        List<Future<String>> futures = new ArrayList<>();

        // Submit tasks to the executor service
        for (int i = 1; i <= 50; i++) {
            int imageId = i;
            Callable<String> task = () -> {
                // Simulate image processing by sleeping for 100ms
                Thread.sleep(100);
                return "Image " + imageId + " processed";
            };
            futures.add(executorService.submit(task));
        }

        // Collect and print results in order
        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Shutdown the executor service
        executorService.shutdown();

        // End time measurement
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        // Print total execution time
        System.out.println("Total execution time: " + totalTime + " milliseconds");
    }
}

