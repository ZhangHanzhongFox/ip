package fox.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fox.exception.FoxException;
import fox.task.Deadline;
import fox.task.Event;

/** JUnit tests for successful and malformed Fox task commands. */
class TaskParserJUnitTest {
    private final TaskParser parser = new TaskParser();

    @Test
    void parsesTodoDeadlineAndEvent() throws FoxException {
        assertEquals("read book", parser.parse("todo", "todo read book").getDescription());
        Deadline deadline = assertInstanceOf(Deadline.class,
                parser.parse("deadline", "deadline submit /by 2026-12-02"));
        Event event = assertInstanceOf(Event.class,
                parser.parse("event", "event meeting /from 10am /to 11am"));

        assertEquals("2026-12-02", deadline.getBy());
        assertEquals("10am", event.getFrom());
        assertEquals("11am", event.getTo());
    }

    @Test
    void rejectsUnknownAndIncompleteCommands() {
        assertThrows(FoxException.class, () -> parser.parse("todo", "todo"));
        assertThrows(FoxException.class, () -> parser.parse("deadline", "deadline submit"));
        assertThrows(FoxException.class, () ->
                parser.parse("event", "event meeting /from 10am"));
        assertThrows(FoxException.class, () ->
                parser.parse("event", "event meeting /from 10am /to"));
        FoxException exception = assertThrows(FoxException.class, () ->
                parser.parse("remove", "remove book"));
        assertEquals("☹ OOPS!!! I'm sorry, but I don't know what that means :-(",
                exception.getMessage());
    }

    @Test
    void rejectsInvalidDeadlineDateAndBlankDescriptions() {
        assertThrows(FoxException.class, () ->
                parser.parse("deadline", "deadline submit /by 2026-02-30"));
        assertThrows(FoxException.class, () ->
                parser.parse("deadline", "deadline /by 2026-12-02"));
        assertThrows(FoxException.class, () ->
                parser.parse("event", "event /from 10am /to 11am"));
    }
}
