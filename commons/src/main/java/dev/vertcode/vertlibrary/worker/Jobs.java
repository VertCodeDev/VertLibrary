/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.worker;

import dev.vertcode.vertlibrary.worker.object.Assignment;
import dev.vertcode.vertlibrary.worker.object.Employee;

import java.util.concurrent.TimeUnit;

/**
 * Credits to Codiq#3662 for the idea to put the Jobs ({@link Employee}) into a enum class.
 */
public enum Jobs {

    //General
    DEFAULT_JOB("Default Job", 4),
    FRONTEND_JOB("Frontend Job", 4),
    BACKEND_JOB("Backend Job", 4),

    //MC
    MC_PLAYER_JOB("Minecraft Player Job", 8),
    MC_PACKET_JOB("Minecraft Packet Job", 12),
    MC_EVENT_JOB("Minecraft Event Job", 24);

    private final Employee employee;

    Jobs(String jobName, int threads) {
        this.employee = new Employee(jobName, threads);
    }

    /**
     * Runs a task
     *
     * @param runnable The task you want to run.
     */
    public void addAssignment(Runnable runnable) {
        this.employee.addAssignment(runnable);
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
        return this.employee.addAssignment(runnable, delay, timeUnit);
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
        return this.employee.addAssignment(runnable, delay, interval, timeUnit);
    }

    /**
     * Get the Employee of this job.
     *
     * @return The {@link Employee}
     */
    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "Jobs{" +
                "employee=" + employee +
                '}';
    }
}
