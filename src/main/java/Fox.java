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
                    System.out.println("     " + (i + 1) + ".[" + tasks[i].getStatusIcon()
                            + "] " + tasks[i].getDescription());
                }
                System.out.println(separator);
            } else if (commandName.equalsIgnoreCase("mark")) {
                markTask(trimmedCommand, tasks, taskCount, separator);
            } else if (commandName.equalsIgnoreCase("unmark")) {
                unmarkTask(trimmedCommand, tasks, taskCount, separator);
            } else {
                if (trimmedCommand.isEmpty()) {
                    continue;
                }
                if (taskCount == tasks.length) {
                    System.out.println(separator);
                    System.out.println("     Your task list is full.");
                    System.out.println(separator);
                    continue;
                }
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println(separator);
                System.out.println("     added: " + command);
                System.out.println(separator);
            }
        }
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
            System.out.println("       [" + tasks[taskIndex].getStatusIcon() + "] "
                    + tasks[taskIndex].getDescription());
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
            System.out.println("       [" + tasks[taskIndex].getStatusIcon() + "] "
                    + tasks[taskIndex].getDescription());
            System.out.println(separator);
        } catch (NumberFormatException exception) {
            printTaskNumberError(separator);
        }
    }
}
