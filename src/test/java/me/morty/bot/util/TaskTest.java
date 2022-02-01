package me.morty.bot.util;


import me.morty.bot.util.Task;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;


class TaskTest {

    @Test
    void testRaceConditions() throws InterruptedException {
        print("Creating first task");
        final Task task = new Task(10L, TimeUnit.SECONDS, doThings(1));
        task.start();

        print("Sleeping for one second");
        Thread.sleep(1000L);

        print("Creating second task");
        final Task task2 = new Task(10L, TimeUnit.SECONDS, doThings(2));
        task2.start();

        print("Waiting for the result...");
        Thread.sleep(15000L);
        print("Done");
    }

    public Runnable doThings(int n) {
        return () -> print(String.format("Task #%d done.", n));
    }

    private void print(String message) {
        System.out.printf("%s - %s%n", new Date(), message);
    }
}