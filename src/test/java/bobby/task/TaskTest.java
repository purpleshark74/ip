package bobby.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests the state and formatting behaviour of {@link Task} objects.
 */
class TaskTest {
    /**
     * Verifies that an uncompleted task is saved with an incomplete status.
     */
    @Test
    void toFileString_newTask_incompleteRecordReturned() {
        Task task = new Task("read book");

        assertEquals("T | 0 | read book", task.toFileString());
    }

    /**
     * Verifies that a completed task is saved with a completed status.
     */
    @Test
    void toFileString_completedTask_completeRecordReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("T | 1 | read book", task.toFileString());
    }

    /**
     * Verifies that serialization preserves an empty description.
     */
    @Test
    void toFileString_emptyDescription_emptyDescriptionRecordReturned() {
        Task task = new Task("");

        assertEquals("T | 0 | ", task.toFileString());
    }

    /**
     * Verifies that a task retains the description supplied at creation.
     */
    @Test
    void getDescription_taskCreated_originalDescriptionReturned() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
    }

    /**
     * Verifies that completing and uncompleting a task changes its status icon.
     */
    @Test
    void statusMethods_taskStateChanges_expectedIconsReturned() {
        Task task = new Task("read book");

        assertEquals(" ", task.getStatusIcon());

        task.markAsDone();
        assertEquals("X", task.getStatusIcon());

        task.markAsNotDone();
        assertEquals(" ", task.getStatusIcon());
    }

    /**
     * Verifies that an incomplete task uses the standard display format.
     */
    @Test
    void toString_newTask_standardIncompleteFormatReturned() {
        Task task = new Task("read book");

        assertEquals("[?][ ] read book", task.toString());
    }

    /**
     * Verifies that a completed task includes its completed status in the display format.
     */
    @Test
    void toString_completedTask_standardCompleteFormatReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("[?][X] read book", task.toString());
    }
}
