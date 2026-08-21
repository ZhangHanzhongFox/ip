/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a new unfinished deadline task.
     *
     * @param description the task description
     * @param by the deadline time
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline task in its display format.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
