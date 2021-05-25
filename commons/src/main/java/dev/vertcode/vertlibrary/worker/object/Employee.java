package dev.vertcode.vertlibrary.worker.object;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This is the Employee, it controls everything.
 */
public class Employee {

    private final UUID uuid;
    private final ScheduledExecutorService executorService;

    public Employee(String jobName, int threads) {
        this.uuid = UUID.randomUUID();
        this.executorService = Executors.newScheduledThreadPool(threads, r -> {
            Thread thread = new Thread(r);
            thread.setName("VertLibrary Employee | " + jobName);
            return thread;
        });
    }

    /**
     * Runs a task
     *
     * @param runnable The task you want to run.
     */
    public void addAssignment(Runnable runnable) {
        this.executorService.submit(runnable);
    }

    /**
     * Create a Assignment for this employee with a initial delay.
     *
     * @param runnable The task you want to run.
     * @param delay    The initial delay you want.
     * @param timeUnit the TimeUnit you want the delay to be in.
     * @return The {@link Assignment} you just created.
     */
    public Assignment addAssignment(Runnable runnable, Long delay, TimeUnit timeUnit) {
        return this.addAssignment(runnable, delay, null, timeUnit);
    }


    /**
     * Create a Assignment for this employee with a initial delay and a interval,
     * this means that it runs every x times per x {@link TimeUnit}.
     *
     * @param runnable The task you want to run.
     * @param delay    The initial delay you want.
     * @param interval The run interval you want.
     * @param timeUnit the TimeUnit you want the delay & interval to be in.
     * @return The {@link Assignment} you just created.
     */
    public Assignment addAssignment(Runnable runnable, Long delay, Long interval, TimeUnit timeUnit) {
        return new Assignment(runnable, delay, interval, timeUnit, this.executorService);
    }

    /**
     * Get the {@link UUID} of this {@link Employee}.
     *
     * @return The {@link UUID} of this {@link Employee}.
     */
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "uuid=" + uuid +
                '}';
    }
}
