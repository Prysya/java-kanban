package test.manager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import main.clients.KVTaskClient;
import main.enums.Endpoints;
import main.enums.TaskStatus;
import main.manager.HTTPTaskManager;
import main.server.KVServer;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.utils.GsonBuilders;
import main.utils.Managers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HTTPTaskManagerTest extends FileBackedTasksManagerTest {
    private static KVServer kvServer;
    private final String KV_SERVER_URL = "http://localhost:8078";
    private final Gson gson = GsonBuilders.getGsonWithLocalDateTime();
    private final KVTaskClient kvTaskClient = new KVTaskClient(KV_SERVER_URL);

    private HTTPTaskManager taskManager;

    @BeforeAll
    static void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterAll
    static void afterAll() {
        kvServer.stop();
    }

    @Override
    HTTPTaskManager createTaskManager() {
        return Managers.getDefault();
    }

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

    <T extends Task> List<T> getListOfTasksFromKvServer(String key, Class<T> rClass) {
        try {
            String json = kvTaskClient.load(key);

            return gson.fromJson(json, TypeToken.getParameterized(ArrayList.class, rClass).getType());
        }
        catch (JsonSyntaxException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Test
    void shouldSaveTasksOnServer() throws IOException, InterruptedException {
        Task task = new Task("title", "desc", TaskStatus.NEW, 10, LocalDateTime.now());
        task.setId(1);

        taskManager.createTask(task);
        taskManager.getTaskById(1);

        List<Task> tasks = getListOfTasksFromKvServer(Endpoints.TASK.getEndpoint(), Task.class);
        assertEquals(tasks.size(), 1);
        assertEquals(tasks.get(0), task);
    }

    @Test
    void shouldSaveEpicsOnServer() throws IOException, InterruptedException {
        Epic epic = new Epic("title", "desc");
        epic.setId(1);

        taskManager.createEpic(epic);
        taskManager.getEpicById(1);

        List<Epic> epics = getListOfTasksFromKvServer(Endpoints.EPIC.getEndpoint(), Epic.class);
        assertEquals(epics.size(), 1);
        assertEquals(epics.get(0), epic);
    }

    @Test
    void shouldSaveSubtasksOnServer() throws IOException, InterruptedException {
        Epic epic = new Epic("title", "desc");
        Subtask subtask = new Subtask("title", "desc", TaskStatus.NEW, 10, LocalDateTime.now(), 1);
        subtask.setId(2);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.getSubtaskById(2);

        List<Subtask> subtasks = getListOfTasksFromKvServer(Endpoints.SUBTASK.getEndpoint(), Subtask.class);
        assertEquals(subtasks.size(), 1);
        assertEquals(subtasks.get(0), subtask);
    }

    @Test
    @Override
    void shouldSaveHistory() {
        taskManager.createTask(new Task("title", "desc", TaskStatus.NEW, 10, LocalDateTime.now()));
        taskManager.createEpic(new Epic("title", "desc"));
        taskManager.createSubtask(new Subtask("title", "desc", TaskStatus.NEW, 10, LocalDateTime.now(), 2));

        taskManager.getTaskById(1);
        assertEquals(getListOfTasksFromKvServer(Endpoints.HISTORY.getEndpoint(), Task.class).size(), 1);

        taskManager.getEpicById(2);
        assertEquals(getListOfTasksFromKvServer(Endpoints.HISTORY.getEndpoint(), Task.class).size(), 2);

        taskManager.getSubtaskById(3);
        assertEquals(getListOfTasksFromKvServer(Endpoints.HISTORY.getEndpoint(), Task.class).size(), 3);
    }

    @Test
    void shouldRestoreFromBackup() {
        taskManager.createTask(new Task("task", "desc", TaskStatus.NEW, 10, LocalDateTime.now()));
        taskManager.createEpic(new Epic("epic", "desc"));
        taskManager.createSubtask(new Subtask(
                "subtask",
                "desc",
                TaskStatus.NEW,
                10,
                LocalDateTime.now().plusHours(1),
                2
        ));
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);

        HTTPTaskManager backupTaskManager = Managers.getDefault();
        assertTrue(backupTaskManager.getTasks().isEmpty());
        assertTrue(backupTaskManager.getEpics().isEmpty());
        assertTrue(backupTaskManager.getSubtasks().isEmpty());
        assertTrue(backupTaskManager.getHistory().isEmpty());

        backupTaskManager.getBackup();
        assertEquals(backupTaskManager.getTasks().size(), 1);
        assertEquals(backupTaskManager.getEpics().size(), 1);
        assertEquals(backupTaskManager.getSubtasks().size(), 1);
        assertEquals(backupTaskManager.getHistory().size(), 3);
    }
}
