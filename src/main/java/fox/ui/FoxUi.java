package fox.ui;

import fox.task.Task;
import fox.task.TaskList;

/** Owns Fox's console presentation while leaving command logic in domain classes. */
public class FoxUi {
    /** The separator printed around Fox's console responses. */
    public static final String SEPARATOR = "    ____________________________________________________________";
    private static final String HAPPY_EXPRESSION = "  /\\_/\\\n ( ^ᴗ^ )\n  > ^ <";
    private static final String SAD_EXPRESSION = "  /\\_/\\\n ( •︵• )\n  > ^ <";

    /** Creates a console presenter for Fox responses. */
    public FoxUi() {
    }

    /**
     * Prints the task list in one-based display order.
     *
     * @param taskList the tasks to display
     */
    public void showTasks(TaskList taskList) {
        System.out.println(SEPARATOR);
        printHappyExpression();
        System.out.println("     Here are the tasks in your list:");
        int number = 1;
        for (Task task : taskList) {
            System.out.println("     " + number++ + "." + task);
        }
        System.out.println(SEPARATOR);
    }

    /**
     * Prints the standard success response for a newly added task.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks after the addition
     */
    public void showAdded(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        printHappyExpression();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        System.out.println(SEPARATOR);
    }

    /**
     * Prints a task mutation response.
     *
     * @param task the task whose completion status changed
     * @param done whether the task was marked done
     */
    public void showMarked(Task task, boolean done) {
        System.out.println(SEPARATOR);
        printHappyExpression();
        System.out.println(done ? "     Nice! I've marked this task as done:"
                : "     OK, I've marked this task as not done yet:");
        System.out.println("       " + task);
        System.out.println(SEPARATOR);
    }

    /**
     * Prints a deletion response.
     *
     * @param task the task that was removed
     * @param remaining the number of tasks left after deletion
     */
    public void showDeleted(Task task, int remaining) {
        System.out.println(SEPARATOR);
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + remaining + " tasks in the list.");
        System.out.println(SEPARATOR);
    }

    /**
     * Prints a command error using Fox's sad expression.
     *
     * @param message the error text to display
     */
    public void showError(String message) {
        System.out.println(SEPARATOR);
        System.out.println(SAD_EXPRESSION);
        System.out.println("     " + message);
        System.out.println(SEPARATOR);
    }

    private void printHappyExpression() {
        System.out.println(HAPPY_EXPRESSION);
    }
}
