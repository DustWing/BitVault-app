package com.bitvault.util;

public class BvUtils {

    /**
     * Simple delay method to pause thread
     *
     * @param delay milliseconds
     */
    public static void delay(int delay) {
        try {
            String name = Thread.currentThread().getName();
            System.out.println("Thread %s Paused...".formatted(name));
            Thread.sleep(delay);
            System.out.println("Thread %s Continue.".formatted(name));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
