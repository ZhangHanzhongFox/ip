package fox.task;

/**
 * Represents a task in Fox's task list.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a new unfinished task.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return {@code X} for a completed task, or a blank space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /** Returns whether this task has been marked as done. */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns this task's status and description for display.
     *
     * @return the formatted task details
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
