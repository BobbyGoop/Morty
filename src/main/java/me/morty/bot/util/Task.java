package me.morty.bot.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Task {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private final Long delay;
    private final TimeUnit unit;
    private final Runnable runnable;

    volatile private ScheduledFuture<?> task = null;

    public Task(Long delay, TimeUnit unit, Runnable runnable) {
        this.delay = delay;
        this.unit = unit;
        this.runnable = runnable;
    }

    public boolean isRunning() {
        if (task != null) {
            return !task.isCancelled();
        }

        return false;
    }

    public void start() {
        stop();
        task = executor.schedule(runnable, delay, unit);
    }

    public void stop() {
        if (task != null) {
            task.cancel(false);
            task = null;
        }
    }
}


