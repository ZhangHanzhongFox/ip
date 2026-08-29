package fox.storage;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import fox.task.Deadline;
import fox.task.Event;
import fox.task.Task;
import fox.task.Todo;

/** Converts task objects into Fox's persisted record format. */
public class TaskStorageCodec {
    private static final String SEPARATOR = "|";

    /** Creates a codec for Fox's persisted task records. */
    public TaskStorageCodec() {
    }

    /**
     * Serializes one task in Fox's Base64-encoded persistence format.
     *
     * @param task the task to serialize; must be a supported non-null task type
     * @return one persisted record without a trailing line separator
     * @throws Storage.StorageException if {@code task} is {@code null} or unsupported
     */
    public String encode(Task task) throws Storage.StorageException {
        if (task == null) {
            throw new Storage.StorageException("Cannot save a missing task.");
        }
        String type;
        String[] fields;
        if (task instanceof Todo) {
            type = "T";
            fields = new String[] {task.getDescription()};
        } else if (task instanceof Deadline deadline) {
            type = "D";
            fields = new String[] {deadline.getDescription(), deadline.getBy()};
        } else if (task instanceof Event event) {
            type = "E";
            fields = new String[] {event.getDescription(), event.getFrom(), event.getTo()};
        } else {
            throw new Storage.StorageException("Cannot save an unsupported task type.");
        }
        StringBuilder result = new StringBuilder(type).append(SEPARATOR)
                .append(task.isDone()).append(SEPARATOR);
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                result.append(SEPARATOR);
            }
            result.append(encodeField(fields[i]));
        }
        return result.toString();
    }

    private String encodeField(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
