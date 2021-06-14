/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.worker.object;

public abstract class AssignmentRunnable implements Runnable {

    private Assignment assignment;

    /**
     * This will initialize the {@link Assignment}
     *
     * @param assignment The {@link Assignment} you want to run in this AssignmentRunnable
     */
    public void initialize(Assignment assignment) {
        this.assignment = assignment;
    }

    /**
     * Returns if the {@link Assignment} is cancelled or not.
     *
     * @return true = cancelled, false not cancelled
     */
    public boolean isCancelled() {
        return this.assignment.isCancelled();
    }

    /**
     * Cancel the {@link Assignment}
     *
     * @param interrupt Should it interrupt the process if its going on?
     * @return true = cancelled, false = couldn't cancel
     */
    public boolean cancel(boolean interrupt) {
        return this.assignment.cancel(interrupt);
    }

    /**
     * Cancel the {@link Assignment}
     *
     * @return true = cancelled, false = couldn't cancel
     */
    public boolean cancel() {
        return this.assignment.cancel(false);
    }

    /**
     * Returns the assignment that is assigned to this {@link AssignmentRunnable}.
     *
     * @return The assigned {@link Assignment}
     */
    public Assignment getAssignment() {
        return assignment;
    }
}
