package fox.parser;

import fox.exception.FoxException;
import fox.task.Deadline;
import fox.task.Event;
import fox.task.FoxDate;
import fox.task.Task;
import fox.task.Todo;

/** Parses task-creation commands without knowing about Fox's UI or storage. */
public class TaskParser {
    /** Creates a parser for Fox task-creation commands. */
    public TaskParser() {
    }

    /**
     * Creates a task from a complete, trimmed task-creation command.
     *
     * @param commandName the first word of {@code command}, such as {@code todo} or {@code deadline}
     * @param command the complete task-creation command
     * @return the task represented by the command
     * @throws FoxException if the command is unknown or has missing or malformed task details
     */
    public Task parse(String commandName, String command) throws FoxException {
        String details = command.substring(commandName.length()).trim();
        if (commandName.equalsIgnoreCase("todo")) {
            return parseTodo(details);
        }
        if (commandName.equalsIgnoreCase("deadline")) {
            return parseDeadline(details);
        }
        if (commandName.equalsIgnoreCase("event")) {
            return parseEvent(details);
        }
        throw new FoxException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    /** Creates a to-do task from its description. */
    private Todo parseTodo(String details) throws FoxException {
        if (details.isEmpty()) {
            throw new FoxException("☹ OOPS!!! The description of a todo cannot be empty.");
        }
        return new Todo(details);
    }

    /** Creates a deadline task from its description and date. */
    private Deadline parseDeadline(String details) throws FoxException {
        String[] parts = details.split("\\s+/by\\s+", 2);
        if (details.isEmpty() || (parts.length == 2 && parts[0].isBlank())) {
            throw new FoxException("☹ OOPS!!! The description of a deadline cannot be empty.");
        }
        if (parts.length != 2 || parts[1].isBlank()) {
            throw new FoxException("☹ OOPS!!! The deadline time cannot be empty.");
        }
        return new Deadline(parts[0].trim(), FoxDate.parse(parts[1].trim()));
    }

    /** Creates an event task from its description, start time, and end time. */
    private Event parseEvent(String details) throws FoxException {
        String[] descriptionAndTimes = details.split("\\s+/from\\s+", 2);
        if (details.isEmpty() || (descriptionAndTimes.length == 2
                && descriptionAndTimes[0].isBlank())) {
            throw new FoxException("☹ OOPS!!! The description of an event cannot be empty.");
        }
        if (descriptionAndTimes.length != 2 || descriptionAndTimes[1].isBlank()) {
            throw new FoxException("☹ OOPS!!! The start time of an event cannot be empty.");
        }
        String[] times = descriptionAndTimes[1].split("\\s+/to\\s+", 2);
        if (times.length != 2 || times[0].isBlank()) {
            throw new FoxException("☹ OOPS!!! The start time of an event cannot be empty.");
        }
        if (times[1].isBlank()) {
            throw new FoxException("☹ OOPS!!! The end time of an event cannot be empty.");
        }
        return new Event(descriptionAndTimes[0].trim(), times[0].trim(), times[1].trim());
    }
}
