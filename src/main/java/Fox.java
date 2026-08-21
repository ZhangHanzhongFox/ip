import java.util.Scanner;

public class Fox {
    private static final String HAPPY_EXPRESSION = "  /\\_/\\\n"
            + " ( ^ᴗ^ )\n"
            + "  > ^ <";
    private static final String SAD_EXPRESSION = "  /\\_/\\\n"
            + " ( •︵• )\n"
            + "  > ^ <";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int taskCount = 0;
        String separator = "    ____________________________________________________________";

        String greeting = "  /\\_/\\\n"
                + " ( •ᴗ• )   Hi! I'm Fox, your little companion. 🦊\n"
                + "  > ^ <    I may be small, but I've got plenty of tricks up my sleeve.\n"
                + "\n"
                + "           What can I do for you?\n";

        String farewell = "  /\\_/\\\n"
                + " ( -.- )   Bye for now! 🌙\n"
                + "  > ^ <    I'm off to the fox den. Wake me up anytime you're in need!\n";

        System.out.print(greeting);
        System.out.println(separator);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            String trimmedCommand = command.trim();
            if (trimmedCommand.isEmpty()) {
                continue;
            }
            String commandName = trimmedCommand.split("\\s+", 2)[0];

            try {
                if (trimmedCommand.equalsIgnoreCase("bye")) {
                    System.out.println(separator);
                    System.out.print(farewell);
                    System.out.println(separator);
                    break;
                } else if (trimmedCommand.equalsIgnoreCase("list")) {
                    System.out.println(separator);
                    printHappyExpression();
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("     " + (i + 1) + "." + tasks[i].toString());
                    }
                    System.out.println(separator);
                } else if (commandName.equalsIgnoreCase("mark")) {
                    markTask(trimmedCommand, tasks, taskCount, separator);
                } else if (commandName.equalsIgnoreCase("unmark")) {
                    unmarkTask(trimmedCommand, tasks, taskCount, separator);
                } else if (commandName.equalsIgnoreCase("delete")) {
                    taskCount = deleteTask(trimmedCommand, tasks, taskCount, separator);
                } else {
                    if (taskCount == tasks.length) {
                        throw new FoxException("☹ OOPS!!! Your task list is full.");
                    }
                    Task task = parseTask(commandName, trimmedCommand);
                    tasks[taskCount] = task;
                    taskCount++;
                    System.out.println(separator);
                    printHappyExpression();
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + task.toString());
                    System.out.println("     Now you have " + taskCount + " tasks in the list.");
                    System.out.println(separator);
                }
            } catch (FoxException exception) {
                System.out.println(separator);
                printSadExpression();
                System.out.println("     " + exception.getMessage());
                System.out.println(separator);
            }
        }
    }

    /**
     * Creates a task from a task-creation command.
     *
     * @param commandName the first word of the command
     * @param command the complete, trimmed command
     * @return the created task
     * @throws FoxException if the command has missing or invalid task details
     */
    private static Task parseTask(String commandName, String command) throws FoxException {
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
            return new Deadline(parts[0].trim(), parts[1].trim());
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

    /**
     * Marks the task selected by a {@code mark <number>} command as done.
     */
    private static void markTask(String command, Task[] tasks, int taskCount, String separator)
            throws FoxException {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new FoxException("☹ OOPS!!! Please provide the task number to mark.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                throw new FoxException("☹ OOPS!!! I couldn't find that task.");
            }

            tasks[taskIndex].markAsDone();
            System.out.println(separator);
            printHappyExpression();
            System.out.println("     Nice! I've marked this task as done:");
            System.out.println("       " + tasks[taskIndex]);
            System.out.println(separator);
        } catch (NumberFormatException exception) {
            throw new FoxException("☹ OOPS!!! The task number must be a whole number.");
        }
    }

    /**
     * Reverses the done status of the task selected by an {@code unmark <number>} command.
     */
    private static void unmarkTask(String command, Task[] tasks, int taskCount, String separator)
            throws FoxException {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new FoxException("☹ OOPS!!! Please provide the task number to unmark.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                throw new FoxException("☹ OOPS!!! I couldn't find that task.");
            }

            tasks[taskIndex].markAsNotDone();
            System.out.println(separator);
            printHappyExpression();
            System.out.println("     OK, I've marked this task as not done yet:");
            System.out.println("       " + tasks[taskIndex]);
            System.out.println(separator);
        } catch (NumberFormatException exception) {
            throw new FoxException(" OOPS!!! The task number must be a whole number.");
        }
    }

    /**
     * Removes the task selected by a {@code delete <number>} command.
     *
     * @param command the complete delete command
     * @param tasks the task list
     * @param taskCount the number of tasks currently in the list
     * @param separator the line used to separate replies
     * @return the number of tasks remaining after the deletion
     * @throws FoxException if the command does not identify an existing task
     */
    private static int deleteTask(String command, Task[] tasks, int taskCount, String separator)
            throws FoxException {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new FoxException("☹ OOPS!!! Please provide the task number to delete.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                throw new FoxException("☹ OOPS!!! I couldn't find that task.");
            }

            Task deletedTask = tasks[taskIndex];
            for (int i = taskIndex; i < taskCount - 1; i++) {
                tasks[i] = tasks[i + 1];
            }
            tasks[taskCount - 1] = null;
            int remainingTaskCount = taskCount - 1;

            System.out.println(separator);
            System.out.println("     Noted. I've removed this task:");
            System.out.println("       " + deletedTask.toString());
            System.out.println("     Now you have " + remainingTaskCount + " tasks in the list.");
            System.out.println(separator);
            return remainingTaskCount;
        } catch (NumberFormatException exception) {
            throw new FoxException("☹ OOPS!!! The task number must be a whole number.");
        }
    }

    /**
     * Prints the expression used with successful task operations.
     */
    private static void printHappyExpression() {
        System.out.println(HAPPY_EXPRESSION);
    }

    /**
     * Prints the expression used when reporting a command error.
     */
    private static void printSadExpression() {
        System.out.println(SAD_EXPRESSION);
    }
}
