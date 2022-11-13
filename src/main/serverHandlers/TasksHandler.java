package main.serverHandlers;

import com.google.gson.Gson;
import main.exceptions.BadRequestException;
import main.exceptions.MethodNotAllowedException;
import main.exceptions.NotFoundException;
import main.manager.HTTPTaskManager;
import main.task.Epic;
import main.task.Task;
import main.utils.UrlHelpers;

import java.util.Objects;

public class TasksHandler implements ServerHandler<Task> {
    private final Gson gson;
    private final HTTPTaskManager taskManager;

    public TasksHandler(Gson gson, HTTPTaskManager taskManager) {
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public String getHandler() {
        return gson.toJson(taskManager.getPrioritizedTasks());
    }

    @Override
    public void postHandler(String json) throws MethodNotAllowedException {
        throw new MethodNotAllowedException();
    }

    @Override
    public void deleteHandler() throws MethodNotAllowedException {
        throw new MethodNotAllowedException();
    }
}
