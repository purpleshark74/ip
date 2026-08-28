package bobby.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests task-list mutation, ordering, and safe task-list access.
 */
class TaskListTest {
    /**
     * Verifies that adding and removing tasks preserves order and updates the list size.
     */
    @Test
    void addAndRemove_tasksAddedAndRemoved_expectedOrderAndSize() {
        Task firstTask = new Todo("first task");
        Task secondTask = new Todo("second task");
        TaskList taskList = new TaskList();

        taskList.add(firstTask);
        taskList.add(secondTask);
        Task removedTask = taskList.remove(0);

        assertEquals(firstTask, removedTask);
        assertEquals(1, taskList.size());
        assertEquals(secondTask, taskList.get(0));
    }

    /**
     * Verifies that mark and unmark operations change only the selected task's state.
     */
    @Test
    void statusMethods_selectedTaskMarkedAndUnmarked_onlySelectedTaskChanges() {
        Task firstTask = new Todo("first task");
        Task secondTask = new Todo("second task");
        TaskList taskList = new TaskList(List.of(firstTask, secondTask));

        taskList.markAsDone(1);
        assertEquals(" ", firstTask.getStatusIcon());
        assertEquals("X", secondTask.getStatusIcon());

        taskList.markAsNotDone(1);
        assertEquals(" ", secondTask.getStatusIcon());
    }

    /**
     * Verifies that the task-list view cannot be modified and does not change after later additions.
     */
    @Test
    void asList_taskListChanges_unmodifiableSnapshotReturned() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("first task"));
        List<Task> snapshot = taskList.asList();

        taskList.add(new Todo("second task"));

        assertEquals(1, snapshot.size());
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(new Todo("third task")));
    }

    /**
     * Verifies that searching descriptions ignores letter case and preserves task order.
     */
    @Test
    void findTasksContaining_matchingDescriptions_matchingTasksInOrderReturned() {
        Task firstTask = new Todo("Read book");
        Task secondTask = new Todo("return book");
        Task thirdTask = new Todo("buy groceries");
        TaskList taskList = new TaskList(List.of(firstTask, secondTask, thirdTask));

        List<Task> matchingTasks = taskList.findTasksContaining("BOOK");

        assertEquals(List.of(firstTask, secondTask), matchingTasks);
        assertThrows(UnsupportedOperationException.class, () -> matchingTasks.add(thirdTask));
    }
}
