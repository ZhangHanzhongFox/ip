package fox.task;

import java.time.LocalDate;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private final LocalDate byDate;
    private final String legacyBy;

    /**
     * Creates a new unfinished deadline task.
     *
     * @param description the task description
     * @param by the deadline time
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.byDate = by;
        this.legacyBy = null;
    }

    /**
     * Creates a legacy deadline whose free-form value is retained for old Fox data.
     * New commands should use the typed constructor.
     */
    public Deadline(String description, String by) {
        super(description);
        this.byDate = null;
        this.legacyBy = by;
    }

    /** Returns the typed deadline date, or {@code null} for legacy free-form data. */
    public LocalDate getByDate() {
        return byDate;
    }

    /** Returns the exact value to write to Fox storage. */
    public String getBy() {
        return byDate == null ? legacyBy : FoxDate.INPUT_FORMAT.format(byDate);
    }

    /**
     * Returns this deadline task in its display format.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        String displayValue = byDate == null ? legacyBy : FoxDate.format(byDate);
        return "[D]" + super.toString() + " (by: " + displayValue + ")";
    }
}
