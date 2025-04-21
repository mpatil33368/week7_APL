import java.util.concurrent.*;
import java.io.*;
import java.util.*;

public class DataProcessingSystem {
    private static final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private static final List<String> results = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Simulate adding tasks
        for (int i = 0; i < 10; i++) {
            taskQueue.put(new Task("Task " + i));
        }

        // Worker threads
        for (int i = 0; i < 4; i++) {
            executor.submit(new Worker(taskQueue, results));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Print results
        results.forEach(System.out::println);
    }
}

class Worker implements Runnable {
    private final BlockingQueue<Task> queue;
    private final List<String> results;

    public Worker(BlockingQueue<Task> queue, List<String> results) {
        this.queue = queue;
        this.results = results;
    }

    @Override
    public void run() {
        try {
            Task task = queue.take();
            // Simulate task processing
            Thread.sleep(1000);
            results.add("Processed: " + task.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Task {
    private final String name;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
