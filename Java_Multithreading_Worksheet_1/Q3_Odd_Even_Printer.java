package Java_Multithreading_Worksheet_1;

import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Q3_Odd_Even_Printer {
    private static final int MAX = 100;
    private int number = 1;
    private final Object lock = new Object();

    @Test
    public static void main(String[] args) {
        Q3_Odd_Even_Printer printer = new Q3_Odd_Even_Printer();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(printer::printOddNumbers);
        executor.submit(printer::printEvenNumbers);

        executor.shutdown();
    }

    @Test
    private void printOddNumbers() {
        while (number < MAX) {
            synchronized (lock) {
                while (number % 2 == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.print(number + " ");
                number++;
                lock.notify();
            }
        }
    }

    @Test
    private void printEvenNumbers() {
        while (number <= MAX) {
            synchronized (lock) {
                while (number % 2 != 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.print(number + " ");
                number++;
                lock.notify();
            }
        }
    }
}
