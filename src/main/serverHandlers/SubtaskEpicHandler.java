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

public class SubtaskEpicHandler implements ServerHandler<Task> {
    private final Integer id;
    private final Gson gson;
    private final HTTPTaskManager taskManager;

    public SubtaskEpicHandler(Integer id, Gson gson, HTTPTaskManager taskManager) {
        this.id = id;
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public String getHandler() throws BadRequestException, NotFoundException {
        UrlHelpers.checkForQueryIdParam(id);

        Epic epic = taskManager.getEpicById(id);

        if (Objects.isNull(epic)) {
            throw new NotFoundException();
        }

        return gson.toJson(taskManager.getEpicSubtasks(epic));
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
