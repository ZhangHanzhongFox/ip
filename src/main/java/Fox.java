import java.util.Scanner;

/** Runs Fox and coordinates input, domain operations, persistence, and presentation. */
public class Fox {
    private static final int MAX_TASKS = 100;
    private final Storage storage;
    private final TaskParser taskParser;
    private final FoxUi ui;
    private TaskList taskList;
    private boolean storageUsable = true;

    /** Creates a Fox application using the default task storage. */
    public Fox() { this(new Storage()); }

    /** Creates a Fox application with injected storage, useful for tests. */
    public Fox(Storage storage) {
        this.storage = storage;
        this.taskParser = new TaskParser();
        this.ui = new FoxUi();
        this.taskList = new TaskList(MAX_TASKS);
    }

    /** Starts Fox's command loop. */
    public void run(Scanner scanner) {
        loadTasks();
        System.out.print("  /\\_/\\\n ( •ᴗ• )   Hi! I'm Fox, your little companion. 🦊\n"
                + "  > ^ <    I may be small, but I've got plenty of tricks up my sleeve.\n\n"
                + "           What can I do for you?\n");
        System.out.println(FoxUi.SEPARATOR);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            if (command.isEmpty()) { continue; }
            try {
                if (command.equalsIgnoreCase("bye")) {
                    saveTasks();
                    System.out.println(FoxUi.SEPARATOR);
                    System.out.print("  /\\_/\\\n ( -.- )   Bye for now! 🌙\n"
                            + "  > ^ <    I'm off to the fox den. Wake me up anytime you're in need!\n");
                    System.out.println(FoxUi.SEPARATOR);
                    return;
                }
                execute(command);
            } catch (FoxException exception) {
                ui.showError(exception.getMessage());
            }
        }
    }

    private void loadTasks() {
        try {
            taskList = new TaskList(storage.load(), MAX_TASKS);
        } catch (Storage.StorageException | FoxException exception) {
            System.out.println("☹ OOPS!!! " + exception.getMessage());
            storageUsable = false;
        }
    }

    private void execute(String command) throws FoxException {
        String commandName = command.split("\\s+", 2)[0];
        if (command.equalsIgnoreCase("list")) {
            ui.showTasks(taskList);
        } else if (commandName.equalsIgnoreCase("mark")) {
            ui.showMarked(taskList.markDone(parseTaskNumber(command, "mark")), true);
            saveTasks();
        } else if (commandName.equalsIgnoreCase("unmark")) {
            ui.showMarked(taskList.markNotDone(parseTaskNumber(command, "unmark")), false);
            saveTasks();
        } else if (commandName.equalsIgnoreCase("delete")) {
            Task deleted = taskList.delete(parseTaskNumber(command, "delete"));
            ui.showDeleted(deleted, taskList.size());
            saveTasks();
        } else {
            Task task = taskParser.parse(commandName, command);
            taskList.add(task);
            saveTasks();
            ui.showAdded(task, taskList.size());
        }
    }

    private int parseTaskNumber(String command, String action) throws FoxException {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new FoxException("☹ OOPS!!! Please provide the task number to " + action + ".");
        }
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            String message = action.equals("unmark")
                    ? " OOPS!!! The task number must be a whole number."
                    : "☹ OOPS!!! The task number must be a whole number.";
            throw new FoxException(message);
        }
    }

    private void saveTasks() {
        if (!storageUsable) { return; }
        try {
            storage.save(taskList.toArray(), taskList.size());
        } catch (Storage.StorageException exception) {
            System.out.println("☹ OOPS!!! " + exception.getMessage());
        }
    }

    /** Application entry point. */
    public static void main(String[] args) { new Fox().run(new Scanner(System.in)); }
}
