package test.task;

import main.enums.TaskStatus;
import main.manager.InMemoryTaskManager;
import main.task.Epic;
import main.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private final int SUBTASK_COUNT = 3;
    private InMemoryTaskManager taskManager;
    private Epic epic;

    @BeforeEach
    void createTaskManager() {
        taskManager = new InMemoryTaskManager();
        taskManager.createEpic(new Epic("title", "description"));
        epic = taskManager.getEpics().get(0);
    }

    private void fillEpicWithSubtasks(TaskStatus taskStatus) {
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

    @Test
    void shouldCalculateEpicTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2022, Month.DECEMBER, 1, 10, 10);
        for (int i = 0; i < 4; i += 1) {
            taskManager.createSubtask(new Subtask("Subtask" + i, "desc" + i, TaskStatus.NEW, 5, localDateTime.plusHours(i), epic.getId()));
        }

        assertEquals(epic.getDuration(), 5 * 4);
        assertEquals(epic.getStartTime(), localDateTime);
        assertEquals(epic.getEndTime(), localDateTime.plusMinutes(5 * 4));
    }

    @Test
    void shouldCalculateEpicTimeIfSubtasksStartTimeIsNull() {
        for (int i = 0; i < 4; i += 1) {
            taskManager.createSubtask(new Subtask("Subtask" + i, "desc" + i, TaskStatus.NEW, 5, null, epic.getId()));
        }

        assertEquals(epic.getDuration(), 5 * 4);
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
    }

    @Test
    void shouldCalculateEpicTimeIfSomeSubtasksStartTimeIsNull() {
        LocalDateTime localDateTime = LocalDateTime.of(2022, Month.DECEMBER, 1, 10, 10);
        for (int i = 0; i < 4; i += 1) {
            taskManager.createSubtask(new Subtask("Subtask" + i, "desc" + i, TaskStatus.NEW, 5, localDateTime.plusHours(i), epic.getId()));
            taskManager.createSubtask(new Subtask("Subtask" + i, "desc" + i, TaskStatus.NEW, 5, null, epic.getId()));
        }

        assertEquals(epic.getDuration(), 5 * 8);
        assertEquals(epic.getStartTime(), localDateTime);
        assertEquals(epic.getEndTime(), localDateTime.plusMinutes(5 * 8));
    }
}