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
            if (details.isEmpty()) {
                throw new FoxException("☹ OOPS!!! The description of a todo cannot be empty.");
            }
            return new Todo(details);
        }
        if (commandName.equalsIgnoreCase("deadline")) {
            String[] parts = details.split("\\s+/by\\s+", 2);
            if (details.isEmpty() || (parts.length == 2 && parts[0].isBlank())) {
                throw new FoxException("☹ OOPS!!! The description of a deadline cannot be empty.");
            }
            if (parts.length != 2 || parts[1].isBlank()) {
                throw new FoxException("☹ OOPS!!! The deadline time cannot be empty.");
            }
            return new Deadline(parts[0].trim(), FoxDate.parse(parts[1].trim()));
        }
        if (commandName.equalsIgnoreCase("event")) {
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
        throw new FoxException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
    }
}
