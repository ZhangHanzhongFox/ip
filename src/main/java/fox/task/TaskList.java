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

    /**
     * Creates an empty task list with the given maximum capacity.
     *
     * @param capacity the maximum number of tasks; must not be negative
     * @throws IllegalArgumentException if {@code capacity} is negative
     */
    public TaskList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Task list capacity cannot be negative.");
        }
        this.capacity = capacity;
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param initialTasks the tasks to place in their existing order
     * @param capacity the maximum number of tasks; must not be negative
     * @throws FoxException if the supplied tasks exceed {@code capacity}
     * @throws NullPointerException if {@code initialTasks} is {@code null}
     */
    public TaskList(Task[] initialTasks, int capacity) throws FoxException {
        this(capacity);
        if (initialTasks.length > capacity) {
            throw new FoxException("The data file contains more than " + capacity + " tasks.");
        }
        for (Task task : initialTasks) {
            tasks.add(task);
        }
    }

    /**
     * Adds a task, rejecting additions beyond the configured capacity.
     *
     * @param task the task to append; may be {@code null}, although callers normally provide a task
     * @throws FoxException if the list has reached its capacity
     */
    public void add(Task task) throws FoxException {
        if (tasks.size() == capacity) {
            throw new FoxException("☹ OOPS!!! Your task list is full.");
        }
        tasks.add(task);
    }

    /**
     * Returns the task using the user's one-based task number.
     *
     * @param taskNumber the one-based position of the requested task
     * @return the task at {@code taskNumber}
     * @throws FoxException if {@code taskNumber} is outside the current list
     */
    public Task get(int taskNumber) throws FoxException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new FoxException("☹ OOPS!!! I couldn't find that task.");
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Marks the selected task as done.
     *
     * @param taskNumber the one-based position of the task to update
     * @return the updated task
     * @throws FoxException if {@code taskNumber} is outside the current list
     */
    public Task markDone(int taskNumber) throws FoxException {
        Task task = get(taskNumber);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the selected task as not done.
     *
     * @param taskNumber the one-based position of the task to update
     * @return the updated task
     * @throws FoxException if {@code taskNumber} is outside the current list
     */
    public Task markNotDone(int taskNumber) throws FoxException {
        Task task = get(taskNumber);
        task.markAsNotDone();
        return task;
    }

    /**
     * Removes and returns the selected task.
     *
     * @param taskNumber the one-based position of the task to remove
     * @return the removed task
     * @throws FoxException if {@code taskNumber} is outside the current list
     */
    public Task delete(int taskNumber) throws FoxException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new FoxException("☹ OOPS!!! I couldn't find that task.");
        }
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return the current number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a snapshot suitable for persistence.
     *
     * @return a new array containing the tasks in list order
     */
    public Task[] toArray() {
        return tasks.toArray(new Task[0]);
    }

    /**
     * Returns an iterator over the tasks in list order.
     *
     * @return an iterator backed by this list
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
