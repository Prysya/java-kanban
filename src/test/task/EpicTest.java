package test.task;

import main.enums.TaskStatus;
import main.manager.InMemoryTaskManager;
import main.task.Epic;
import main.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    final int SUBTASK_COUNT = 3;
    InMemoryTaskManager taskManager;
    Epic epic;

    @BeforeEach
    void createTaskManager() {
        taskManager = new InMemoryTaskManager();
        taskManager.createEpic(new Epic("title", "description"));
        epic = taskManager.getEpics().get(0);
    }

    void fillEpicWithSubtasks(TaskStatus taskStatus) {
        for (int i = 0; i < SUBTASK_COUNT; i += 1) {
            taskManager.createSubtask(new Subtask("title", "description", taskStatus, 0, null, epic.getId()));
        }
    }

    @Test
    void newEpicHasEmptySubtaskLists() {
        Epic[] emptyArray = new Epic[0];

        assertTrue(epic.getSubtaskIds().isEmpty());
        assertArrayEquals(epic.getSubtaskIds().toArray(), emptyArray);
    }

    @Test
    void EpicHasNewStatusWithNewSubtasks() {
        fillEpicWithSubtasks(TaskStatus.NEW);

        assertEquals(epic.getSubtaskIds().size(), SUBTASK_COUNT);
        assertEquals(epic.getTaskStatus(), TaskStatus.NEW);
    }

    @Test
    void EpicHasDoneStatusWithDoneSubtasks() {
        fillEpicWithSubtasks(TaskStatus.DONE);

        assertEquals(epic.getSubtaskIds().size(), SUBTASK_COUNT);
        assertEquals(epic.getTaskStatus(), TaskStatus.DONE);
    }

    @Test
    void EpicHasInProgressStatusWithNewAndDoneSubtasks() {
        fillEpicWithSubtasks(TaskStatus.NEW);
        fillEpicWithSubtasks(TaskStatus.DONE);

        assertEquals(epic.getSubtaskIds().size(), SUBTASK_COUNT * 2);
        assertEquals(epic.getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void EpicHasInProgressStatusWithInProgressSubtasks() {
        fillEpicWithSubtasks(TaskStatus.IN_PROGRESS);

        assertEquals(epic.getSubtaskIds().size(), SUBTASK_COUNT);
        assertEquals(epic.getTaskStatus(), TaskStatus.IN_PROGRESS);
    }
}