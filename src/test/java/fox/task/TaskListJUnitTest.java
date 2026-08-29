package fox.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fox.exception.FoxException;

/** JUnit tests for task-list indexing, mutation, deletion, and capacity. */
class TaskListJUnitTest {
    @Test
    void supportsOneBasedAccessAndMutations() throws FoxException {
        Task first = new Todo("first");
        Task second = new Todo("second");
        TaskList list = new TaskList(new Task[] {first, second}, 2);

        assertEquals(first, list.get(1));
        assertEquals(second, list.get(2));
        assertEquals(first, list.markDone(1));
        assertEquals(first, list.delete(1));
        assertEquals(1, list.size());
        assertEquals(second, list.get(1));
    }

    @Test
    void rejectsInvalidIndexesAndOverflow() throws FoxException {
        TaskList list = new TaskList(1);
        list.add(new Todo("only task"));

        assertThrows(FoxException.class, () -> list.get(0));
        assertThrows(FoxException.class, () -> list.get(2));
        assertThrows(FoxException.class, () -> list.delete(-1));
        assertThrows(FoxException.class, () -> list.add(new Todo("overflow")));
    }

    @Test
    void rejectsNegativeCapacityAndOversizedInitialData() {
        assertThrows(IllegalArgumentException.class, () -> new TaskList(-1));
        assertThrows(FoxException.class,
                () -> new TaskList(new Task[] {new Todo("one")}, 0));
    }
}
