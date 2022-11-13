package test.manager;

import main.enums.TaskStatus;
import main.manager.InMemoryHistoryManager;
import main.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private final int TASKS_COUNT = 5;
    private InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    void createHistoryManager() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    private void fillHistoryByTasks(InMemoryHistoryManager manager) {

        IntStream.iterate(1, i -> i + 1).limit(TASKS_COUNT).forEach(index -> {
            Task task = new Task(String.valueOf(index), "desc", TaskStatus.NEW, 10, null);
            task.setId(index);
            manager.add(task);
        });
    }

    @Test
    void shouldReturnEmptyHistory() {
        assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
    }

    @Test
    void shouldReturnFilledHistory() {
        inMemoryHistoryManager.add(new Task("test", "test", TaskStatus.NEW, 10, null));
        Task task = new Task("test2", "test2", TaskStatus.NEW, 10, null);
        task.setId(1);
        inMemoryHistoryManager.add(task);
        assertEquals(inMemoryHistoryManager.getHistory().size(), 2);
    }

    @Test
    void shouldAddTask() {
        Task task = new Task("test", "test", TaskStatus.NEW, 10, null);
        inMemoryHistoryManager.add(task);
        assertEquals(inMemoryHistoryManager.getHistory().get(0), task);
    }

    @Test
    void shouldUpdateTask() {
        final String OLD_TITLE = "old_title";
        final String NEW_TITLE = "new_title";

        Task task = new Task(OLD_TITLE, "test", TaskStatus.NEW, 10, null);
        inMemoryHistoryManager.add(task);
        assertEquals(inMemoryHistoryManager.getHistory().get(0).getTitle(), OLD_TITLE);

        task.setTitle(NEW_TITLE);
        inMemoryHistoryManager.add(task);
        assertEquals(inMemoryHistoryManager.getHistory().get(0).getTitle(), NEW_TITLE);
    }

    @Test
    void shouldRemoveHistoryFromStart() {
        fillHistoryByTasks(inMemoryHistoryManager);
        assertEquals(inMemoryHistoryManager.getHistory().size(), TASKS_COUNT);

        inMemoryHistoryManager.remove(1);
        assertEquals(inMemoryHistoryManager.getHistory().size(), TASKS_COUNT - 1);
        assertEquals(inMemoryHistoryManager.getHistory().get(0).getId(), 2);
    }

    @Test
    void shouldRemoveHistoryFromCenter() {
        fillHistoryByTasks(inMemoryHistoryManager);
        assertEquals(inMemoryHistoryManager.getHistory().size(), TASKS_COUNT);

        inMemoryHistoryManager.remove(3);

        assertEquals(inMemoryHistoryManager.getHistory().get(1).getId(), 2);
        assertEquals(inMemoryHistoryManager.getHistory().get(2).getId(), 4);
    }

    @Test
    void shouldRemoveHistoryFromEnd() {
        fillHistoryByTasks(inMemoryHistoryManager);
        assertEquals(inMemoryHistoryManager.getHistory().size(), TASKS_COUNT);

        assertEquals(inMemoryHistoryManager.getHistory().get(TASKS_COUNT - 1).getId(), TASKS_COUNT);

        inMemoryHistoryManager.remove(TASKS_COUNT);

        final IndexOutOfBoundsException exception = assertThrows(
                IndexOutOfBoundsException.class,
                () -> inMemoryHistoryManager.getHistory().get(TASKS_COUNT - 1)
        );

        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }
}