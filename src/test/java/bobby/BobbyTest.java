package bobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;

import bobby.task.TaskList;
import bobby.task.Todo;

/**
 * Tests Bobby's command responses independently of either user interface.
 */
class BobbyTest {
    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-09-10T04:00:00Z"), ZoneId.of("Asia/Singapore"));

    /**
     * Verifies that listing tasks returns their numbered display forms.
     */
    @Test
    void getResponse_listCommand_numberedTaskListReturned() {
        Bobby bobby = new Bobby(new TaskList(List.of(
                new Todo("read book"),
                new Todo("write essay"))));

        String response = bobby.getResponse("list");

        assertEquals("Here are the tasks in your list:\n"
                + "     1.[T][ ] read book\n"
                + "     2.[T][ ] write essay", response);
    }

    /**
     * Verifies that finding tasks returns matching descriptions only.
     */
    @Test
    void getResponse_findCommand_matchingTasksReturned() {
        Bobby bobby = new Bobby(new TaskList(List.of(
                new Todo("Read book"),
                new Todo("buy groceries"))));

        String response = bobby.getResponse("find book");

        assertEquals("Here are the matching tasks in your list:\n"
                + "     1.[T][ ] Read book", response);
    }

    /**
     * Verifies that statistics use the current calendar week and current task states.
     */
    @Test
    void getResponse_statsCommand_currentTaskStatisticsReturned() {
        Todo completedThisWeek = new Todo("read book");
        completedThisWeek.markAsDone(LocalDateTime.of(2026, 9, 7, 0, 0));
        Todo completedBeforeThisWeek = new Todo("submit essay");
        completedBeforeThisWeek.markAsDone(LocalDateTime.of(2026, 9, 6, 23, 59, 59));
        Todo completedAtUnknownTime = new Todo("legacy task");
        completedAtUnknownTime.markAsDone();
        Todo pendingTask = new Todo("buy groceries");
        Bobby bobby = new Bobby(new TaskList(List.of(
                completedThisWeek, completedBeforeThisWeek, completedAtUnknownTime, pendingTask)),
                FIXED_CLOCK);

        String response = bobby.getResponse("stats");

        assertEquals("Here are your task statistics:\n"
                + "     Period: Sep 07 2026 to Sep 13 2026\n"
                + "     Currently completed this week: 1\n"
                + "     Completed overall: 3\n"
                + "     Pending: 1\n"
                + "     Total: 4", response);
    }

    /**
     * Verifies that statistics for an empty task list contain zero-valued counts.
     */
    @Test
    void getResponse_statsCommandWithEmptyList_zeroStatisticsReturned() {
        Bobby bobby = new Bobby(new TaskList(), FIXED_CLOCK);

        String response = bobby.getResponse("stats");

        assertEquals("Here are your task statistics:\n"
                + "     Period: Sep 07 2026 to Sep 13 2026\n"
                + "     Currently completed this week: 0\n"
                + "     Completed overall: 0\n"
                + "     Pending: 0\n"
                + "     Total: 0", response);
    }

    /**
     * Verifies that invalid input is converted into a user-facing response.
     */
    @Test
    void getResponse_invalidCommand_errorResponseReturned() {
        Bobby bobby = new Bobby(new TaskList());

        String response = bobby.getResponse("unknown");

        assertEquals("     I don't understand what you said. Please use the correct commands", response);
    }

    /**
     * Verifies that the exit command receives Bobby's farewell response.
     */
    @Test
    void getResponse_byeCommand_farewellResponseReturned() {
        Bobby bobby = new Bobby(new TaskList());

        String response = bobby.getResponse("bye");

        assertEquals("     Bye! Hope to see you again soon.", response);
    }

    /**
     * Verifies that only the exit command requests application termination.
     */
    @Test
    void isExitCommand_variedCommands_expectedExitDecisionsReturned() {
        Bobby bobby = new Bobby(new TaskList());

        assertTrue(bobby.isExitCommand("  ByE  "));
        assertFalse(bobby.isExitCommand("bye now"));
        assertFalse(bobby.isExitCommand("list"));
    }
}
