package main.manager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.clients.KVTaskClient;
import main.enums.Endpoints;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.utils.GsonBuilders;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class HTTPTaskManager extends FileBackedTasksManager {
    private static Gson gson;
    private final KVTaskClient kvTaskClient;

    public HTTPTaskManager(String kvServerUrl) {
        kvTaskClient = new KVTaskClient(kvServerUrl);
        gson = GsonBuilders.getGsonWithLocalDateTime();
    }

    @Override
    protected void save() {
        kvTaskClient.put(Endpoints.TASK.getEndpoint(), gson.toJson(this.getTasks()));
        kvTaskClient.put(Endpoints.EPIC.getEndpoint(), gson.toJson(this.getEpics()));
        kvTaskClient.put(Endpoints.SUBTASK.getEndpoint(), gson.toJson(this.getSubtasks()));
        kvTaskClient.put(Endpoints.HISTORY.getEndpoint(), gson.toJson(this.getHistory()));
    }

    private <T extends Task> List<T> loadFromServer(String key, Type type) {
        String json = kvTaskClient.load(key);
        if (!Objects.isNull(json)) {
            try {
                T[] tasks = gson.fromJson(json, type);
                return List.of(tasks);
            } catch (JsonSyntaxException ignored) {
            }
        }
        return null;

    }

    public void getBackup() {
        List<Task> tasks = loadFromServer(Endpoints.TASK.getEndpoint(), Task[].class);
        if (!Objects.isNull(tasks)) {
            tasks.forEach(this::createTask);
        }

        List<Epic> epics = loadFromServer(Endpoints.EPIC.getEndpoint(), Epic[].class);
        if (!Objects.isNull(epics)) {
            epics.forEach(this::createEpic);
        }

        List<Subtask> subtasks = loadFromServer(Endpoints.SUBTASK.getEndpoint(), Subtask[].class);
        if (!Objects.isNull(subtasks)) {
            subtasks.forEach(this::createSubtask);
        }

        List<Task> history = loadFromServer(Endpoints.HISTORY.getEndpoint(), Task[].class);
        if (!Objects.isNull(history)) {
            history.forEach(task -> {
                if (Task.class.equals(task.getClass())) {
                    this.getTaskById(task.getId());
                } else if (Epic.class.equals(task.getClass())) {
                    this.getEpicById(task.getId());
                } else if (Subtask.class.equals(task.getClass())) {
                    this.getSubtaskById(task.getId());
                }
            });
        }
    }
}

