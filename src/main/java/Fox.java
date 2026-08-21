import java.util.Scanner;

public class Fox {
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

            if (trimmedCommand.equalsIgnoreCase("bye")) {
                System.out.println(separator);
                System.out.print(farewell);
                System.out.println(separator);
                break;
            } else if (trimmedCommand.equalsIgnoreCase("list")) {
                System.out.println(separator);
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i].toString());
                }
                System.out.println(separator);
            } else if (commandName.equalsIgnoreCase("mark")) {
                markTask(trimmedCommand, tasks, taskCount, separator);
            } else if (commandName.equalsIgnoreCase("unmark")) {
                unmarkTask(trimmedCommand, tasks, taskCount, separator);
            } else {
                if (taskCount == tasks.length) {
                    System.out.println(separator);
                    System.out.println("     Your task list is full.");
                    System.out.println(separator);
                    continue;
                }
                Task task = parseTask(commandName, trimmedCommand);
                if (task == null) {
                    continue;
                }
                tasks[taskCount] = task;
                taskCount++;
                System.out.println(separator);
                System.out.println("     Got it. I've added this task:");
                System.out.println("       " + task.toString());
                System.out.println("     Now you have " + taskCount + " tasks in the list.");
                System.out.println(separator);
            }
        }
    }

    /**
     * Creates a task from a task-creation command.
     *
     * @param commandName the first word of the command
     * @param command the complete, trimmed command
     * @return the created task, or {@code null} when the command is incomplete
     */
    private static Task parseTask(String commandName, String command) {
        String details = command.substring(commandName.length()).trim();

        if (commandName.equalsIgnoreCase("todo")) {
            if (details.isEmpty()) {
                printTaskFormatError("todo <description>");
                return null;
            }
            return new Todo(details);
        }

        if (commandName.equalsIgnoreCase("deadline")) {
            String[] parts = details.split("\\s+/by\\s+", 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                printTaskFormatError("deadline <description> /by <time>");
                return null;
            }
            return new Deadline(parts[0].trim(), parts[1].trim());
        }

        if (commandName.equalsIgnoreCase("event")) {
            String[] descriptionAndTimes = details.split("\\s+/from\\s+", 2);
            if (descriptionAndTimes.length != 2 || descriptionAndTimes[0].isBlank()) {
                printTaskFormatError("event <description> /from <start> /to <end>");
                return null;
            }

            String[] times = descriptionAndTimes[1].split("\\s+/to\\s+", 2);
            if (times.length != 2 || times[0].isBlank() || times[1].isBlank()) {
                printTaskFormatError("event <description> /from <start> /to <end>");
                return null;
            }
            return new Event(descriptionAndTimes[0].trim(), times[0].trim(), times[1].trim());
        }

        return new Task(command);
    }

    /**
     * Displays the required format for an incomplete task-creation command.
     *
     * @param format the expected command format
     */
    private static void printTaskFormatError(String format) {
        System.out.println("     Use: " + format);
    }

    /**
     * Marks the task selected by a {@code mark <number>} command as done.
     */
    private static void markTask(String command, Task[] tasks, int taskCount, String separator) {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            printTaskNumberError(separator);
            return;
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                printTaskNumberError(separator);
                return;
            }

            tasks[taskIndex].markAsDone();
            System.out.println(separator);
            System.out.println("     Nice! I've marked this task as done:");
            System.out.println("       " + tasks[taskIndex]);
            System.out.println(separator);
        } catch (NumberFormatException exception) {
            printTaskNumberError(separator);
        }
    }

    private static void printTaskNumberError(String separator) {
        System.out.println(separator);
        System.out.println("     I couldn't find that task.");
        System.out.println(separator);
    }

    /**
     * Reverses the done status of the task selected by an {@code unmark <number>} command.
     */
    private static void unmarkTask(String command, Task[] tasks, int taskCount, String separator) {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            printTaskNumberError(separator);
            return;
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                printTaskNumberError(separator);
                return;
            }

            tasks[taskIndex].markAsNotDone();
            System.out.println(separator);
            System.out.println("     OK, I've marked this task as not done yet:");
            System.out.println("       " + tasks[taskIndex]);
            System.out.println(separator);
        } catch (NumberFormatException exception) {
            printTaskNumberError(separator);
        }
    }
}
