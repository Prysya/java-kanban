package main.serverHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.exceptions.BadRequestException;
import main.exceptions.NotFoundException;
import main.manager.HTTPTaskManager;
import main.task.Task;
import main.utils.UrlHelpers;

import java.util.Objects;

public class TaskHandler implements ServerHandler<Task> {
    private final Integer id;
    private final Gson gson;
    private final HTTPTaskManager taskManager;

    public TaskHandler(Integer id, Gson gson, HTTPTaskManager taskManager) {
        this.id = id;
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public String getHandler() throws BadRequestException, NotFoundException {
        if (UrlHelpers.checkForQueryIdParam(id)) {
            return gson.toJson(taskManager.getTasks());
        } else {
            Task task = taskManager.getTaskById(id);

            if (Objects.isNull(task)) {
                throw new NotFoundException();
            }

            return gson.toJson(task);
        }
    }

    @Override
    public void postHandler(String body) throws BadRequestException {
        try {
            Task task = gson.fromJson(body, Task.class);
            taskManager.createTask(task);
        }
        catch (JsonSyntaxException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public void deleteHandler() throws BadRequestException, NotFoundException {
        if (UrlHelpers.checkForQueryIdParam(id)) {
            taskManager.deleteAllTasks();
        } else {
            if (Objects.isNull(taskManager.getTaskById(id))) {
                throw new NotFoundException();
            }

            taskManager.deleteTask(id);
        }
    }
}
