package test.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import main.enums.Endpoints;
import main.enums.TaskStatus;
import main.server.HttpTaskServer;
import main.server.KVServer;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.utils.GsonBuilders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = GsonBuilders.getGsonWithLocalDateTime();
    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;
    private final String PRIORITIZED_TASKS_URL = "http://localhost:8080/tasks/";
    private final String HISTORY_URL = PRIORITIZED_TASKS_URL + Endpoints.HISTORY.getEndpoint();
    private final String TASKS_URL = PRIORITIZED_TASKS_URL + Endpoints.TASK.getEndpoint();
    private final String EPICS_URL = PRIORITIZED_TASKS_URL + Endpoints.EPIC.getEndpoint();
    private final String SUBTASKS_URL = PRIORITIZED_TASKS_URL + Endpoints.SUBTASK.getEndpoint();

    @BeforeEach
    void startServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    void stopServers() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    HttpResponse<String> sendPostRequest(URI uri, String json) throws IOException, InterruptedException {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    HttpResponse<String> sendGetRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    void sendDeleteRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    JsonElement parseResToJson(HttpResponse<String> response) {
        return JsonParser.parseString(response.body());
    }

    String buildQueryWithId(String url, int id) {
        return url + "?id=" + id;
    }

    <T extends Task> List<T> getListOfTasks(String endpoint, Class<T> rClass) throws IOException, InterruptedException {
        HttpResponse<String> tasks = sendGetRequest(URI.create(endpoint));
        JsonElement tasksJson = parseResToJson(tasks);

        return gson.fromJson(tasksJson, TypeToken.getParameterized(ArrayList.class, rClass).getType());
    }

    @Test
    void shouldCreateTaskByServer() throws IOException, InterruptedException {
        HttpResponse<String> response = sendPostRequest(URI.create(TASKS_URL), gson.toJson(new Task(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now())));
        assertEquals(response.statusCode(), 201);
    }

    @Test
    void shouldCreateEpicByServer() throws IOException, InterruptedException {
        HttpResponse<String> response = sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));
        assertEquals(response.statusCode(), 201);
    }

    @Test
    void shouldCreateSubtaskByServer() throws IOException, InterruptedException {
        shouldCreateEpicByServer();
        HttpResponse<String> response = sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(new Subtask(Endpoints.SUBTASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now().plusHours(10), 1)));
        assertEquals(response.statusCode(), 201);
    }

    @Test
    void shouldGetTaskByIdFromServer() throws IOException, InterruptedException {
        sendPostRequest(URI.create(TASKS_URL), gson.toJson(new Task(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now())));

        HttpResponse<String> response = sendGetRequest(URI.create(buildQueryWithId(TASKS_URL, 1)));
        JsonElement json = parseResToJson(response);

        assertEquals(response.statusCode(), 200);

        Task task = gson.fromJson(json, Task.class);

        assertEquals(task.getId(), 1);
        assertEquals(task.getTitle(), Endpoints.TASK.getEndpoint());
    }

    @Test
    void shouldGetEpicByIdFromServer() throws IOException, InterruptedException {
        sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));

        HttpResponse<String> response = sendGetRequest(URI.create(buildQueryWithId(EPICS_URL, 1)));
        JsonElement json = parseResToJson(response);

        assertEquals(response.statusCode(), 200);

        Epic epic = gson.fromJson(json, Epic.class);

        assertEquals(epic.getId(), 1);
        assertEquals(epic.getTitle(), Endpoints.EPIC.getEndpoint());
    }

    @Test
    void shouldGetSubtaskByIdFromServer() throws IOException, InterruptedException {
        sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));
        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(new Subtask(Endpoints.SUBTASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now().plusHours(1), 1)));

        HttpResponse<String> response = sendGetRequest(URI.create(buildQueryWithId(SUBTASKS_URL, 2)));
        JsonElement json = parseResToJson(response);

        assertEquals(response.statusCode(), 200);

        Subtask subtask = gson.fromJson(json, Subtask.class);

        assertEquals(subtask.getId(), 2);
        assertEquals(subtask.getTitle(), Endpoints.SUBTASK.getEndpoint());
    }

    @Test
    void shouldReturnListOfTasks() throws IOException, InterruptedException {
        assertTrue(getListOfTasks(TASKS_URL, Task.class).isEmpty());

        sendPostRequest(URI.create(TASKS_URL), gson.toJson(new Task(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now())));
        sendPostRequest(URI.create(TASKS_URL), gson.toJson(new Task(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now().plusHours(1))));
        assertEquals(getListOfTasks(TASKS_URL, Task.class).size(), 2);
    }

    @Test
    void shouldReturnListOfEpics() throws IOException, InterruptedException {
        assertTrue(getListOfTasks(EPICS_URL, Epic.class).isEmpty());

        sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));
        sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));
        assertEquals(getListOfTasks(EPICS_URL, Task.class).size(), 2);
    }

    @Test
    void shouldReturnListOfSubtasks() throws IOException, InterruptedException {
        assertTrue(getListOfTasks(SUBTASKS_URL, Subtask.class).isEmpty());

        sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));
        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(new Subtask(Endpoints.SUBTASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now(), 1)));
        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(new Subtask(Endpoints.SUBTASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now().plusHours(1), 1)));
        assertEquals(getListOfTasks(SUBTASKS_URL, Task.class).size(), 2);
    }

    @Test
    void shouldDeleteTask() throws IOException, InterruptedException {
        sendPostRequest(URI.create(TASKS_URL), gson.toJson(new Task(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now())));
        assertEquals(getListOfTasks(TASKS_URL, Task.class).size(), 1);

        sendDeleteRequest(URI.create(buildQueryWithId(TASKS_URL, 1)));
        assertTrue(getListOfTasks(TASKS_URL, Task.class).isEmpty());
    }

    @Test
    void shouldDeleteEpic() throws IOException, InterruptedException {
        sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));
        assertEquals(getListOfTasks(EPICS_URL, Epic.class).size(), 1);

        sendDeleteRequest(URI.create(buildQueryWithId(EPICS_URL, 1)));
        assertTrue(getListOfTasks(EPICS_URL, Epic.class).isEmpty());
    }

    @Test
    void shouldDeleteSubtask() throws IOException, InterruptedException {
        sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));
        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(new Subtask(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now(), 1)));
        assertEquals(getListOfTasks(SUBTASKS_URL, Subtask.class).size(), 1);

        sendDeleteRequest(URI.create(buildQueryWithId(SUBTASKS_URL, 2)));
        assertTrue(getListOfTasks(SUBTASKS_URL, Subtask.class).isEmpty());
    }

    @Test
    void shouldReturnEpicSubtasksList() throws IOException, InterruptedException {
        sendPostRequest(URI.create(EPICS_URL), gson.toJson(new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle")));

        assertTrue(getListOfTasks(buildQueryWithId(SUBTASKS_URL + "/" + Endpoints.EPIC.getEndpoint(), 1), Subtask.class).isEmpty());

        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(new Subtask(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now(), 1)));
        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(new Subtask(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now().plusHours(1), 1)));

        assertEquals(getListOfTasks(buildQueryWithId(SUBTASKS_URL + "/" + Endpoints.EPIC.getEndpoint(), 1), Subtask.class).size(), 2);
    }

    @Test
    void shouldReturnListOfHistory() throws IOException, InterruptedException {
        assertTrue(getListOfTasks(HISTORY_URL, Task.class).isEmpty());

        Task task = new Task(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now());
        Epic epic = new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle");
        Subtask subtask = new Subtask(Endpoints.SUBTASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, LocalDateTime.now().plusHours(10), 2);

        sendPostRequest(URI.create(TASKS_URL), gson.toJson(task));
        sendPostRequest(URI.create(EPICS_URL), gson.toJson(epic));
        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(subtask));

        sendGetRequest(URI.create(buildQueryWithId(TASKS_URL, 1)));
        sendGetRequest(URI.create(buildQueryWithId(EPICS_URL, 2)));
        sendGetRequest(URI.create(buildQueryWithId(SUBTASKS_URL, 3)));

        List<Task> tasks = getListOfTasks(HISTORY_URL, Task.class);
        assertEquals(tasks.size(), 3);
    }

    @Test
    void shouldReturnPreoritizedTasks() throws IOException, InterruptedException {
        assertTrue(getListOfTasks(PRIORITIZED_TASKS_URL, Task.class).isEmpty());

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timePlusTenHours = LocalDateTime.now().plusHours(10);

        Task task = new Task(Endpoints.TASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, timeNow);
        Epic epic = new Epic(Endpoints.EPIC.getEndpoint(), "Subtitle");
        Subtask subtask = new Subtask(Endpoints.SUBTASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, timePlusTenHours, 2);
        Subtask subtask1 = new Subtask(Endpoints.SUBTASK.getEndpoint(), "Subtitle", TaskStatus.NEW, 10, null, 2);

        sendPostRequest(URI.create(TASKS_URL), gson.toJson(task));
        sendPostRequest(URI.create(EPICS_URL), gson.toJson(epic));
        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(subtask));
        sendPostRequest(URI.create(SUBTASKS_URL), gson.toJson(subtask1));

        List<Task> tasks = getListOfTasks(PRIORITIZED_TASKS_URL, Task.class);
        assertEquals(tasks.size(), 3);
        assertEquals(tasks.get(0).getStartTime(), timeNow);
        assertEquals(tasks.get(1).getStartTime(), timePlusTenHours);
        assertNull(tasks.get(2).getStartTime());
    }
}