package lhf2018.utils;

import java.util.concurrent.*;

public class ThreadPoolUtil {
    public static final ExecutorService executorService = new ThreadPoolExecutor(7, 15, 1,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(5, true),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void run(Runnable task){
        executorService.execute(task);
    }
}
