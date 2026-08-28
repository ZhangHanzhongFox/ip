package fox.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fox.exception.FoxException;

/**
 * Encapsulates Fox's ordered collection of tasks and its capacity rules.
 */
public class TaskList implements Iterable<Task> {
    private final int capacity;
    private final List<Task> tasks;

    /** Creates an empty task list with the given maximum capacity. */
    public TaskList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Task list capacity cannot be negative.");
        }
        this.capacity = capacity;
        this.tasks = new ArrayList<>();
    }

    /** Creates a task list containing the supplied tasks. */
    public TaskList(Task[] initialTasks, int capacity) throws FoxException {
        this(capacity);
        if (initialTasks.length > capacity) {
            throw new FoxException("The data file contains more than " + capacity + " tasks.");
        }
        for (Task task : initialTasks) {
            tasks.add(task);
        }
    }

    /** Adds a task, rejecting additions beyond the configured capacity. */
    public void add(Task task) throws FoxException {
        if (tasks.size() == capacity) {
            throw new FoxException("☹ OOPS!!! Your task list is full.");
        }
        tasks.add(task);
    }

    /** Returns the task using the user's one-based task number. */
    public Task get(int taskNumber) throws FoxException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new FoxException("☹ OOPS!!! I couldn't find that task.");
        }
        return tasks.get(taskNumber - 1);
    }

    /** Marks the selected task as done. */
    public Task markDone(int taskNumber) throws FoxException {
        Task task = get(taskNumber);
        task.markAsDone();
        return task;
    }

    /** Marks the selected task as not done. */
    public Task markNotDone(int taskNumber) throws FoxException {
        Task task = get(taskNumber);
        task.markAsNotDone();
        return task;
    }

    /** Removes and returns the selected task. */
    public Task delete(int taskNumber) throws FoxException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new FoxException("☹ OOPS!!! I couldn't find that task.");
        }
        return tasks.remove(taskNumber - 1);
    }

    /** Returns the number of tasks in this list. */
    public int size() {
        return tasks.size();
    }

    /** Returns a snapshot suitable for persistence. */
    public Task[] toArray() {
        return tasks.toArray(new Task[0]);
    }

    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
