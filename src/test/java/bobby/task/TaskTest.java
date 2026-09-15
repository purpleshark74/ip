package bobby.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

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

        assertEquals("T | 0 | read book | -", task.toFileString());
    }

    /**
     * Verifies that a completed task is saved with a completed status.
     */
    @Test
    void toFileString_completedTask_completeRecordReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("T | 1 | read book | -", task.toFileString());
    }

    /**
     * Verifies that completion timestamps are truncated, preserved, cleared, and replaced as specified.
     */
    @Test
    void completionDateTime_statusChanges_expectedCompletionDateTimeStored() {
        Task task = new Task("read book");
        LocalDateTime firstCompletion = LocalDateTime.of(2026, 9, 8, 10, 15, 30, 123_000_000);
        LocalDateTime secondCompletion = LocalDateTime.of(2026, 9, 9, 11, 20, 45);

        task.markAsDone(firstCompletion);
        task.markAsDone(secondCompletion);

        assertEquals("T | 1 | read book | 2026-09-08T10:15:30", task.toFileString());
        assertEquals(true, task.hasKnownCompletionDateTime());

        task.markAsNotDone();

        assertEquals("T | 0 | read book | -", task.toFileString());
        assertEquals(false, task.hasKnownCompletionDateTime());

        task.markAsDone(secondCompletion);

        assertEquals("T | 1 | read book | 2026-09-09T11:20:45", task.toFileString());
    }

    /**
     * Verifies that tasks reject missing and blank descriptions.
     */
    @Test
    void constructor_invalidDescription_assertionErrorThrown() {
        assertThrows(AssertionError.class, () -> new Task(""));
        assertThrows(AssertionError.class, () -> new Task("   "));
        assertThrows(AssertionError.class, () -> new Task(null));
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

    /**
     * Verifies that a deadline labels and formats its deadline date and time.
     */
    @Test
    void toString_deadlineTask_formattedDeadlineReturned() {
        LocalDateTime deadlineDateTime = LocalDateTime.of(2026, 9, 1, 14, 0);
        Deadline deadline = new Deadline("return book", deadlineDateTime);

        assertEquals("[D][ ] return book (appointed for: Sep 01 2026 2:00 PM)", deadline.toString());
    }

    /**
     * Verifies that an event labels and formats its start and end date-times.
     */
    @Test
    void toString_eventTask_formattedStartAndEndReturned() {
        LocalDateTime startDateTime = LocalDateTime.of(2026, 9, 1, 14, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2026, 9, 1, 16, 0);
        Event event = new Event("meeting", startDateTime, endDateTime);

        assertEquals("[E][ ] meeting (commencing: Sep 01 2026 2:00 PM; concluding: Sep 01 2026 4:00 PM)",
                event.toString());
    }

    /**
     * Verifies that date-based tasks reject missing date-time values.
     */
    @Test
    void dateBasedTask_nullDateTime_assertionErrorThrown() {
        LocalDateTime validDateTime = LocalDateTime.of(2026, 9, 1, 14, 0);

        assertThrows(AssertionError.class, () -> new Deadline("return book", null));
        assertThrows(AssertionError.class, () -> new Event("meeting", null, validDateTime));
        assertThrows(AssertionError.class, () -> new Event("meeting", validDateTime, null));
    }

    /**
     * Verifies that an event constructor enforces a strictly increasing time range.
     */
    @Test
    void event_invalidTimeRange_assertionErrorThrown() {
        LocalDateTime startDateTime = LocalDateTime.of(2026, 9, 1, 14, 0);

        assertThrows(AssertionError.class, () ->
                new Event("meeting", startDateTime, startDateTime));
        assertThrows(AssertionError.class, () ->
                new Event("meeting", startDateTime, startDateTime.minusMinutes(1)));
    }
}
