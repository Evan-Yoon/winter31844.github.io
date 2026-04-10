package javaStudy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Study07 {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        executorService.shutdownNow();
    }
}
