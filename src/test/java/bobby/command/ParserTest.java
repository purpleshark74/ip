package bobby.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bobby.exception.BobbyException;
import bobby.task.Deadline;
import bobby.task.Event;
import bobby.task.Todo;

/**
 * Tests command parsing and input validation in {@link Parser}.
 */
class ParserTest {
    /**
     * Verifies that the exit command ignores surrounding whitespace and letter case.
     */
    @Test
    void isByeCommand_validExitCommand_trueReturned() {
        assertEquals(true, Parser.isByeCommand("  ByE  "));
    }

    /**
     * Verifies that a command that merely contains the exit word is not an exit command.
     */
    @Test
    void isByeCommand_nonExitCommand_falseReturned() {
        assertEquals(false, Parser.isByeCommand("bye now"));
    }

    /**
     * Verifies that a list command does not carry task data.
     */
    @Test
    void parse_listCommand_listCommandReturned() throws BobbyException {
        Parser.Command command = Parser.parse(" LIST ", 3);

        assertEquals(Parser.CommandType.LIST, command.getType());
        assertNull(command.getTask());
        assertEquals(-1, command.getTaskIndex());
    }

    /**
     * Verifies that a find command returns its trimmed keyword without task data.
     */
    @Test
    void parse_findCommand_findCommandWithKeywordReturned() throws BobbyException {
        Parser.Command command = Parser.parse(" FIND   book  ", 3);

        assertEquals(Parser.CommandType.FIND, command.getType());
        assertEquals("book", command.getKeyword());
        assertNull(command.getTask());
        assertEquals(-1, command.getTaskIndex());
    }

    /**
     * Verifies that a to-do command produces a to-do task with a trimmed description.
     */
    @Test
    void parse_todoCommand_todoTaskReturned() throws BobbyException {
        Parser.Command command = Parser.parse("todo   read book  ", 0);

        assertEquals(Parser.CommandType.ADD, command.getType());
        Todo task = assertInstanceOf(Todo.class, command.getTask());
        assertEquals("read book", task.getDescription());
        assertEquals(-1, command.getTaskIndex());
    }

    /**
     * Verifies that a deadline command parses its description and date-time.
     */
    @Test
    void parse_deadlineCommand_deadlineTaskReturned() throws BobbyException {
        Parser.Command command = Parser.parse("deadline return book /by 2026-09-01 1400", 0);

        assertEquals(Parser.CommandType.ADD, command.getType());
        Deadline task = assertInstanceOf(Deadline.class, command.getTask());
        assertEquals("D | 0 | return book | 2026-09-01T14:00", task.toFileString());
    }

    /**
     * Verifies that an event command parses its description, start, and end date-times.
     */
    @Test
    void parse_eventCommand_eventTaskReturned() throws BobbyException {
        Parser.Command command = Parser.parse(
                "event project meeting /from 2026-09-01 1400 /to 2026-09-01 1600", 0);

        assertEquals(Parser.CommandType.ADD, command.getType());
        Event task = assertInstanceOf(Event.class, command.getTask());
        assertEquals("E | 0 | project meeting | 2026-09-01T14:00 | 2026-09-01T16:00",
                task.toFileString());
    }

    /**
     * Verifies that one-based task numbers are converted to zero-based indices.
     */
    @Test
    void parse_taskCommands_validTaskNumbers_zeroBasedIndicesReturned() throws BobbyException {
        Parser.Command markCommand = Parser.parse("mark 1", 3);
        Parser.Command unmarkCommand = Parser.parse("unmark 2", 3);
        Parser.Command deleteCommand = Parser.parse("delete 3", 3);

        assertEquals(Parser.CommandType.MARK, markCommand.getType());
        assertEquals(0, markCommand.getTaskIndex());
        assertEquals(Parser.CommandType.UNMARK, unmarkCommand.getType());
        assertEquals(1, unmarkCommand.getTaskIndex());
        assertEquals(Parser.CommandType.DELETE, deleteCommand.getType());
        assertEquals(2, deleteCommand.getTaskIndex());
    }

    /**
     * Verifies that task commands reject missing, non-numeric, and out-of-range task numbers.
     */
    @Test
    void parse_taskCommands_invalidTaskNumber_exceptionThrown() {
        assertInvalidTaskNumber("mark", 1);
        assertInvalidTaskNumber("unmark zero", 1);
        assertInvalidTaskNumber("delete 0", 1);
        assertInvalidTaskNumber("mark 2", 1);
    }

    /**
     * Verifies that a to-do command requires a description.
     */
    @Test
    void parse_todoWithoutDescription_exceptionThrown() {
        BobbyException exception = assertThrows(BobbyException.class, () -> Parser.parse("todo", 0));

        assertEquals("You don't have a task after the todo.", exception.getMessage());
    }

    /**
     * Verifies that a find command requires a keyword.
     */
    @Test
    void parse_findWithoutKeyword_exceptionThrown() {
        BobbyException exception = assertThrows(BobbyException.class, () -> Parser.parse("find", 0));

        assertEquals("Please provide a keyword to search for.", exception.getMessage());
    }

    /**
     * Verifies that date-based commands reject invalid dates and missing components.
     */
    @Test
    void parse_dateBasedCommandInvalidDetails_exceptionThrown() {
        BobbyException invalidDateException = assertThrows(BobbyException.class,
                () -> Parser.parse("deadline submit report /by 2026-02-29 1200", 0));
        BobbyException missingEventDetailsException = assertThrows(BobbyException.class,
                () -> Parser.parse("event meeting /from 2026-09-01 1400", 0));

        assertEquals("Please use dates and times in YYYY-MM-DD HHMM format.",
                invalidDateException.getMessage());
        assertEquals("Please use: event DESCRIPTION /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM",
                missingEventDetailsException.getMessage());
    }

    /**
     * Verifies that unknown commands and command-word prefixes are rejected.
     */
    @Test
    void parse_unknownCommand_exceptionThrown() {
        assertUnknownCommand("remind me");
        assertUnknownCommand("todoing read book");
    }

    /**
     * Verifies that a command reports the standard invalid-task-number message.
     *
     * @param input the command to parse
     * @param taskCount the number of tasks available to the command
     */
    private void assertInvalidTaskNumber(String input, int taskCount) {
        BobbyException exception = assertThrows(BobbyException.class, () -> Parser.parse(input, taskCount));

        assertEquals("Invalid task number.", exception.getMessage());
    }

    /**
     * Verifies that a command reports the standard unknown-command message.
     *
     * @param input the command to parse
     */
    private void assertUnknownCommand(String input) {
        BobbyException exception = assertThrows(BobbyException.class, () -> Parser.parse(input, 0));

        assertEquals("I don't understand what you said. Please use the correct commands",
                exception.getMessage());
    }
}
