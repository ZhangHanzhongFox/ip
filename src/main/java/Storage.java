import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Reads and writes Fox's tasks from a local text file.
 *
 * <p>Each task is stored on one line as {@code type|done|field...}; task
 * fields are Base64 encoded so that descriptions and times may contain any
 * normal text, including the separator character.</p>
 */
public class Storage {
    /** The default data file, relative to the directory from which Fox starts. */
    public static final Path DEFAULT_FILE = Path.of("data", "fox.txt");
    private static final String SEPARATOR = "|";

    private final Path dataFile;

    /** Creates storage using the default relative data-file path. */
    public Storage() {
        this(DEFAULT_FILE);
    }

    /** Creates storage using a caller-supplied path, useful for tests. */
    public Storage(Path dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * Loads all saved tasks, creating the parent directory if it is absent.
     *
     * @return loaded tasks, or an empty array when the file is absent or empty
     * @throws StorageException if the file cannot be read or is invalid
     */
    public Task[] load() throws StorageException {
        try {
            Path parent = dataFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(dataFile)) {
                return new Task[0];
            }
            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();
            for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
                String line = lines.get(lineNumber);
                if (line.isBlank()) {
                    continue;
                }
                tasks.add(parseLine(line, lineNumber + 1, dataFile));
            }
            return tasks.toArray(new Task[0]);
        } catch (IOException exception) {
            throw new StorageException("Could not read data file '" + dataFile + "'.", exception);
        }
    }

    /**
     * Saves tasks atomically, preserving the previous file if writing fails.
     *
     * @param tasks the task array
     * @param taskCount number of occupied entries in the array
     * @throws StorageException if the data cannot be written
     */
    public void save(Task[] tasks, int taskCount) throws StorageException {
        try {
            Path parent = dataFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < taskCount; i++) {
                lines.add(formatTask(tasks[i]));
            }
            Path temporaryFile = Files.createTempFile(parent == null ? Path.of(".") : parent,
                    dataFile.getFileName().toString(), ".tmp");
            try {
                Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
                try {
                    Files.move(temporaryFile, dataFile, StandardCopyOption.ATOMIC_MOVE,
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException atomicMoveFailure) {
                    Files.move(temporaryFile, dataFile, StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                Files.deleteIfExists(temporaryFile);
            }
        } catch (IOException exception) {
            throw new StorageException("Could not write data file '" + dataFile + "'.", exception);
        }
    }

    private static String formatTask(Task task) throws StorageException {
        if (task == null) {
            throw new StorageException("Cannot save a missing task.");
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
            throw new StorageException("Cannot save an unsupported task type.");
        }
        StringBuilder result = new StringBuilder(type).append(SEPARATOR)
                .append(task.isDone()).append(SEPARATOR);
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                result.append(SEPARATOR);
            }
            result.append(encode(fields[i]));
        }
        return result.toString();
    }

    private static Task parseLine(String line, int lineNumber, Path dataFile) throws StorageException {
        String[] parts = line.split("\\|", -1);
        try {
            if (parts.length < 3 || !parts[1].equals("true") && !parts[1].equals("false")) {
                throw new IllegalArgumentException("invalid status or field count");
            }
            boolean done = Boolean.parseBoolean(parts[1]);
            Task task;
            switch (parts[0]) {
            case "T":
                requireFieldCount(parts, 3);
                String todoDescription = decode(parts[2]);
                requireNonBlank(todoDescription);
                task = new Todo(todoDescription);
                break;
            case "D":
                requireFieldCount(parts, 4);
                String deadlineDescription = decode(parts[2]);
                String deadlineTime = decode(parts[3]);
                requireNonBlank(deadlineDescription);
                requireNonBlank(deadlineTime);
                task = new Deadline(deadlineDescription, deadlineTime);
                break;
            case "E":
                requireFieldCount(parts, 5);
                String eventDescription = decode(parts[2]);
                String eventFrom = decode(parts[3]);
                String eventTo = decode(parts[4]);
                requireNonBlank(eventDescription);
                requireNonBlank(eventFrom);
                requireNonBlank(eventTo);
                task = new Event(eventDescription, eventFrom, eventTo);
                break;
            default:
                throw new IllegalArgumentException("unknown task type");
            }
            if (done) {
                task.markAsDone();
            }
            return task;
        } catch (IllegalArgumentException exception) {
            throw new StorageException("Invalid data on line " + lineNumber
                    + " in '" + dataFile + "'.", exception);
        }
    }

    private static void requireFieldCount(String[] parts, int expected) {
        if (parts.length != expected) {
            throw new IllegalArgumentException("invalid field count");
        }
    }

    private static void requireNonBlank(String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("a task field cannot be empty");
        }
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }

    /** A user-facing storage or data-format failure. */
    public static class StorageException extends Exception {
        private static final long serialVersionUID = 1L;
        /** Creates a storage failure with a concise message. */
        public StorageException(String message) {
            super(message);
        }

        /** Creates a storage failure while retaining the underlying cause. */
        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
