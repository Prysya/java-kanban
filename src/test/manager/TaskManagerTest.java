package test.manager;

import main.enums.TaskStatus;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    private final int TASK_COUNT = 3;
    private T manager;

    abstract T createTaskManager();

    @BeforeEach
    void createManagerForEachTest() {
        manager = createTaskManager();
    }

    Task createTask() {
        return new Task("test", "test", TaskStatus.NEW, 0, null);
    }

    Epic createEpic() {
        return new Epic("test", "test");
    }

    Subtask createSubtask(Epic epic) {
        return new Subtask("test", "test", TaskStatus.NEW, 0, null, epic.getId());
    }

    void fillManagerWithTasks() {
        for (int i = 0; i < TASK_COUNT; i += 1) {
            manager.createTask(new Task("title", "desc", TaskStatus.NEW, 10, null));
        }
    }

    void fillManagerWithEpics() {
        for (int i = 0; i < TASK_COUNT; i += 1) {
            manager.createEpic(new Epic("title", "desc"));
        }
    }

    void fillManagerWithSubtasks(Epic epic, TaskStatus taskStatus) {
        for (int i = 0; i < TASK_COUNT; i += 1) {
            manager.createSubtask(new Subtask("title", "description", taskStatus, 10, null, epic.getId()));
        }
    }

    @Test
    void generateIdShouldReturnUniqueInt() {
        assertEquals(manager.generateId(), 1);
        assertEquals(manager.generateId(), 2);
        assertEquals(manager.generateId(), 3);
    }

    @Test
    void newManagerShouldReturnEmptyTasksList() {
        Task[] tasks = new Task[0];

        assertTrue(manager.getTasks().isEmpty());
        assertArrayEquals(manager.getTasks().toArray(), tasks);
    }

    @Test
    void filledBy3TasksManagerShouldReturnListWith3Tasks() {
        fillManagerWithTasks();

        assertEquals(manager.getTasks().size(), TASK_COUNT);
    }

    @Test
    void newManagerShouldReturnEmptyEpicsList() {
        Epic[] epics = new Epic[0];

        assertTrue(manager.getEpics().isEmpty());
        assertArrayEquals(manager.getEpics().toArray(), epics);
    }

    @Test
    void filledBy3EpicsManagerShouldReturnListWith3Epics() {
        fillManagerWithEpics();

        assertEquals(manager.getEpics().size(), TASK_COUNT);
    }

    @Test
    void newManagerShouldReturnEmptySubtasksList() {
        Subtask[] subtasks = new Subtask[0];

        assertTrue(manager.getSubtasks().isEmpty());
        assertArrayEquals(manager.getSubtasks().toArray(), subtasks);
    }

    @Test
    void filledBy3SubtasksManagerShouldReturnListWith3Subtasks() {
        fillManagerWithEpics();
        fillManagerWithSubtasks(manager.getEpics().get(0), TaskStatus.NEW);

        assertEquals(manager.getSubtasks().size(), TASK_COUNT);
    }

    @Test
    void shouldDeleteAllTasks() {
        fillManagerWithTasks();
        assertEquals(manager.getTasks().size(), TASK_COUNT);

        manager.deleteAllTasks();
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void shouldDeleteAllEpics() {
        fillManagerWithEpics();
        assertEquals(manager.getEpics().size(), TASK_COUNT);

        manager.deleteAllEpics();
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void shouldDeleteAllSubtasksWithEpics() {
        fillManagerWithEpics();
        fillManagerWithSubtasks(manager.getEpics().get(0), TaskStatus.NEW);

        assertEquals(manager.getEpics().size(), TASK_COUNT);
        assertEquals(manager.getSubtasks().size(), TASK_COUNT);

        manager.deleteAllEpics();
        assertTrue(manager.getEpics().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    void shouldDeleteAllSubtasksAlsoShouldDeleteSubtaskIdsInEpic() {
        fillManagerWithEpics();
        fillManagerWithSubtasks(manager.getEpics().get(0), TaskStatus.NEW);

        assertEquals(manager.getSubtasks().size(), TASK_COUNT);

        manager.deleteAllSubtasks();
        assertTrue(manager.getSubtasks().isEmpty());
        assertTrue(manager.getEpics().get(0).getSubtaskIds().isEmpty());
    }

    @Test
    void shouldReturnCreatedTaskById() {
        Task task = createTask();
        manager.createTask(task);

        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void shouldReturnNullIfManagerHasNoTaskById() {
        assertNull(manager.getTaskById(0));
    }

    @Test
    void shouldReturnCreatedEpicById() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    void shouldReturnNullIfManagerHasNoEpicById() {
        assertNull(manager.getEpicById(0));
    }

    @Test
    void shouldReturnCreatedSubtaskById() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(subtask);

        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }

    @Test
    void shouldReturnNullIfManagerHasNoSubtaskById() {
        assertNull(manager.getSubtaskById(0));
    }

    @Test
    void shouldCreateTask() {
        Task task = createTask();

        manager.createTask(task);

        assertEquals(manager.getTasks().size(), 1);
        assertEquals(manager.getTasks().get(0), task);
    }

    @Test
    void shouldNotCreateTaskIfTaskIsNull() {
        manager.createTask(null);

        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void shouldCreateEpic() {
        Epic epic = createEpic();

        manager.createEpic(epic);

        assertEquals(manager.getEpics().size(), 1);
        assertEquals(manager.getEpics().get(0), epic);
    }

    @Test
    void shouldNotCreateEpicIfEpicIsNull() {
        manager.createEpic(null);

        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void shouldCreateSubtask() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(subtask);

        assertEquals(manager.getSubtasks().size(), 1);
        assertEquals(manager.getSubtasks().get(0), subtask);
    }

    @Test
    void shouldNotCreateSubtaskIfSubtaskIsNull() {
        Epic epic = createEpic();

        manager.createEpic(epic);
        manager.createSubtask(null);

        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    void shouldUpdateTask() {
        Task task = createTask();
        manager.createTask(task);
        assertEquals(task.getTaskStatus(), TaskStatus.NEW);

        task.setTaskStatus(TaskStatus.DONE);
        manager.updateTask(task);
        assertEquals(task.getTaskStatus(), TaskStatus.DONE);
    }

    @Test
    void shouldBreakUpdateIfTaskIsNull() {
        manager.updateTask(null);
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void shouldUpdateEpic() {
        final String oldTitle = "test";
        final String newTitle = "test-2";

        Epic epic = new Epic(oldTitle, "test");
        manager.createEpic(epic);
        assertEquals(epic.getTitle(), oldTitle);

        epic.setTitle(newTitle);
        manager.updateEpic(epic);
        assertEquals(epic.getTitle(), newTitle);
    }

    @Test
    void shouldBreakUpdateIfEpicIsNull() {
        manager.updateEpic(null);
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void shouldUpdateSubtask() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(subtask);
        assertEquals(subtask.getTaskStatus(), TaskStatus.NEW);

        subtask.setTaskStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask);
        assertEquals(subtask.getTaskStatus(), TaskStatus.DONE);
    }

    @Test
    void shouldBreakUpdateIfSubtaskIsNull() {
        manager.updateTask(null);
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void shouldBreakUpdateIfSubtaskParentEpicIsNotDefined() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(subtask);
        assertEquals(subtask.getTaskStatus(), TaskStatus.NEW);
        assertEquals(subtask.getParentEpicId(), epic.getId());

        subtask.setParentEpicId(0);
        manager.updateSubtask(subtask);
        assertEquals(subtask.getTaskStatus(), TaskStatus.NEW);
    }

    @Test
    void shouldDeleteTask() {
        Task task = createTask();
        manager.createTask(task);

        assertEquals(manager.getTasks().size(), 1);
        manager.deleteTask(task.getId());

        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void shouldDeleteEpic() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        assertEquals(manager.getEpics().size(), 1);
        manager.deleteEpic(epic.getId());

        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void shouldDeleteSubtask() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(subtask);

        assertEquals(manager.getSubtasks().size(), 1);
        manager.deleteSubtask(subtask.getId());

        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    void shouldReturnEpicSubtaskIds() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        assertTrue(manager.getEpicSubtasks(epic).isEmpty());

        for (int i = 1; i <= TASK_COUNT; i += 1) {
            Subtask subtask = createSubtask(epic);
            manager.createSubtask(subtask);
            assertEquals(manager.getEpicSubtasks(epic).size(), i);
        }
    }

    @Test
    void shouldReturnEmptyListEpicSubtaskIdsIfEpicInNull() {
        assertTrue(manager.getEpicSubtasks(null).isEmpty());
    }

    @Test
    void shouldUpdateEpicStatusAfterSubtaskAddition() {
        manager.createEpic(new Epic("title", "desc"));
        assertEquals(manager.getEpics().get(0).getTaskStatus(), TaskStatus.NEW);

        fillManagerWithSubtasks(manager.getEpics().get(0), TaskStatus.IN_PROGRESS);
        assertEquals(manager.getEpics().get(0).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldUpdateEpicStatusAfterSubtaskDelete() {
        manager.createEpic(new Epic("title", "desc"));
        fillManagerWithSubtasks(manager.getEpics().get(0), TaskStatus.DONE);
        assertEquals(manager.getEpics().get(0).getTaskStatus(), TaskStatus.DONE);

        manager.deleteAllSubtasks();
        assertEquals(manager.getEpics().get(0).getTaskStatus(), TaskStatus.NEW);
    }

    @Test
    void shouldAddTaskInTaskTree() {
        assertTrue(manager.getPrioritizedTasks().isEmpty());

        Task task = new Task("Title", "description", TaskStatus.NEW, 10, LocalDateTime.now());
        manager.createTask(task);

        assertEquals(manager.getPrioritizedTasks().size(), 1);
        assertEquals(manager.getPrioritizedTasks().get(0), task);
    }

    @Test
    void shouldSortTasksInTaskTreeByStartDate() {
        for (int i = 0; i < 10; i += 1) {
            Task task = new Task(String.valueOf(i), "description", TaskStatus.NEW, 10, LocalDateTime.now().minusDays(i));
            manager.createTask(task);
        }

        assertEquals(manager.getPrioritizedTasks().get(0).getId(), 10);
        assertEquals(manager.getPrioritizedTasks().get(9).getId(), 1);
    }

    @Test
    void shouldSetTasksWithStartTimeIsNullToTheEnd() {
        for (int i = 1; i <= 4; i += 1) {
            Task task = new Task("Title" + i, "description" + i, TaskStatus.NEW, 10, i % 2 == 0 ? LocalDateTime.now() : null);
            manager.createTask(task);
        }

        assertEquals(manager.getPrioritizedTasks().get(0).getId(), 2);
        assertEquals(manager.getPrioritizedTasks().get(1).getId(), 4);
        assertEquals(manager.getPrioritizedTasks().get(2).getId(), 1);
        assertEquals(manager.getPrioritizedTasks().get(3).getId(), 3);
    }

    @Test
    void shouldAddSubtaskInTaskTree() {
        assertTrue(manager.getPrioritizedTasks().isEmpty());
        manager.createEpic(createEpic());

        Subtask subtask = new Subtask("Title", "description", TaskStatus.NEW, 10, LocalDateTime.now(), manager.getEpics().get(0).getId());
        manager.createSubtask(subtask);

        assertEquals(manager.getPrioritizedTasks().size(), 1);
        assertEquals(manager.getPrioritizedTasks().get(0), subtask);
    }

    @Test
    void shouldSortSubtasksInTaskTreeByStartDate() {
        manager.createEpic(createEpic());
        for (int i = 0; i < 10; i += 1) {
            Subtask subtask = new Subtask("Title", "description", TaskStatus.NEW, 10, LocalDateTime.now().minusDays(i), manager.getEpics().get(0).getId());
            manager.createSubtask(subtask);
        }

        assertEquals(manager.getPrioritizedTasks().get(0).getId(), 11);
        assertEquals(manager.getPrioritizedTasks().get(9).getId(), 2);
    }

    @Test
    void shouldSetSubtaskWithStartTimeIsNullToTheEnd() {
        manager.createEpic(createEpic());
        for (int i = 1; i <= 4; i += 1) {
            Subtask subtask = new Subtask("Title" + i, "description", TaskStatus.NEW, 10, i % 2 == 0 ? LocalDateTime.now() : null, manager.getEpics().get(0).getId());
            manager.createSubtask(subtask);
        }

        assertEquals(manager.getPrioritizedTasks().get(0).getId(), 3);
        assertEquals(manager.getPrioritizedTasks().get(1).getId(), 5);
        assertEquals(manager.getPrioritizedTasks().get(2).getId(), 2);
        assertEquals(manager.getPrioritizedTasks().get(3).getId(), 4);
    }

    @Test
    void shouldDoNotAddTaskAndSubtaskWithSameTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Task task = new Task("Test", "test", TaskStatus.NEW, 10, localDateTime);
        manager.createTask(task);
        manager.createEpic(createEpic());
        for (int i = 1; i <= 4; i += 1) {
            Subtask subtask = new Subtask("Title" + i, "description", TaskStatus.NEW, 10, localDateTime, manager.getEpics().get(0).getId());
            manager.createSubtask(subtask);
        }

        List<Task> treeList = manager.getPrioritizedTasks();

        assertEquals(treeList.size(), 1);
        assertEquals(treeList.get(0), task);
    }

    @Test
    void shouldDoNotAddSubtaskWithSameTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        manager.createEpic(createEpic());
        Subtask subtask = new Subtask("Title", "description", TaskStatus.NEW, 10, localDateTime, manager.getEpics().get(0).getId());
        manager.createSubtask(subtask);

        for (int i = 1; i <= 4; i += 1) {
            manager.createSubtask(new Subtask("Title" + i, "description", TaskStatus.NEW, 10, localDateTime, manager.getEpics().get(0).getId()));
        }

        List<Task> treeList = manager.getPrioritizedTasks();

        assertEquals(treeList.size(), 1);
        assertEquals(treeList.get(0), subtask);
    }
}
