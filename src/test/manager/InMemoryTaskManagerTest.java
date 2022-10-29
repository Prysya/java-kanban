package test.manager;

import main.enums.TaskStatus;
import main.manager.InMemoryHistoryManager;
import main.manager.InMemoryTaskManager;
import main.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManager inMemoryTaskManager;

    @Override
    InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void createInMemoryTaskManager() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldReturnInMemoryHistoryManager() {
        assertEquals(inMemoryTaskManager.getInMemoryHistoryManager().getClass(), InMemoryHistoryManager.class);
    }

    @Test
    void shouldReturnEmptyHistoryListOfTasks() {
        assertTrue(inMemoryTaskManager.getHistory().isEmpty());
    }

    @Test
    void shouldReturnFilledHistoryListOfTasks() {
        final int TASKS_COUNT = 5;
        for (int i = 0; i < TASKS_COUNT; i += 1) {
            Task task = new Task("task" + i, "task", TaskStatus.NEW, 10, null);
            manager.createTask(task);
            manager.getTaskById(task.getId());
        }

        assertEquals(manager.getHistory().size(), TASKS_COUNT);
        assertEquals(manager.getHistory().get(0).getTitle(), "task0");
    }
}
