package bobby;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import bobby.task.TaskList;
import bobby.task.Todo;

/**
 * Tests Bobby's command responses independently of either user interface.
 */
class BobbyTest {
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
}
