package com.bitvault.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadProvider {

    private final ExecutorService mExecutorService;

    public static ThreadProvider create() {

        int cores = Runtime.getRuntime().availableProcessors();

        final ThreadFactory threadFactory = new ThreadFactory() {
            final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

            public Thread newThread(Runnable r) {
                Thread thread = this.defaultFactory.newThread(r);
                thread.setName("AppThread-" + thread.getName());
                return thread;
            }
        };

        final ExecutorService executor = Executors.newFixedThreadPool(
                cores,
                threadFactory
        );

        return new ThreadProvider(executor);
    }

    private ThreadProvider(ExecutorService executorService) {
        this.mExecutorService = executorService;
    }


    public ExecutorService executor() {
        return mExecutorService;
    }
}
