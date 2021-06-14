/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.worker.object;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This is just a typical stupid {@link Assignment} that a {@link Employee} gets from his boss.
 */
public class Assignment {

    private final UUID uuid;
    private final Runnable runnable;
    private final TimeUnit timeUnit;
    private final Long delay;
    private final Long interval;
    private ScheduledFuture<?> scheduledFuture;
    private Future<?> future;

    public Assignment(Runnable runnable, Long delay, Long interval, TimeUnit timeUnit, ScheduledExecutorService executorService) {
        this.uuid = UUID.randomUUID();
        this.runnable = runnable;
        this.delay = delay;
        this.interval = interval;
        this.timeUnit = timeUnit;

        if (runnable instanceof AssignmentRunnable) ((AssignmentRunnable) runnable).initialize(this);


        if (delay != null && interval == null) {
            this.future = executorService.schedule(runnable, delay, timeUnit);
            return;
        }

        if (interval == null || delay == null)
            throw new NullPointerException("Interval & Delay cannot be null at the same time!");
        this.scheduledFuture = executorService.scheduleAtFixedRate(runnable, delay, interval, timeUnit);
    }

    /**
     * Cancel a {@link Assignment}.
     *
     * @return Returns if the {@link Assignment} could be cancelled or not!
     */
    public boolean cancel() {
        return this.cancel(false);
    }

    /**
     * Cancel a {@link Assignment}.
     *
     * @param interrupt Should it interrupt the process its running?
     * @return Returns if the {@link Assignment} could be cancelled or not!
     */
    public boolean cancel(boolean interrupt) {
        if (this.future != null) return this.future.cancel(interrupt);
        else return this.scheduledFuture.cancel(interrupt);
    }

    /**
     * Check if the {@link Assignment} is cancelled.
     *
     * @return Returns if the {@link Assignment} is cancelled.
     */
    public boolean isCancelled() {
        return (this.future != null && this.future.isCancelled()) ||
                (this.scheduledFuture != null && this.scheduledFuture.isCancelled());
    }

    /**
     * Check if the {@link Assignment} is done.
     *
     * @return Returns if the {@link Assignment} is done.
     */
    public boolean isDone() {
        return this.future != null && this.future.isDone();
    }

    /**
     * Get the {@link UUID} of this {@link Assignment}.
     *
     * @return The {@link UUID} of this {@link Assignment}.
     */
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "uuid=" + uuid +
                '}';
    }
}
