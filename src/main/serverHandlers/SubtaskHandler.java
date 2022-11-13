package main.serverHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import main.exceptions.BadRequestException;
import main.exceptions.NotFoundException;
import main.manager.HTTPTaskManager;
import main.task.Subtask;
import main.task.Task;
import main.utils.UrlHelpers;

import java.util.ArrayList;
import java.util.Objects;

public class SubtaskHandler implements ServerHandler<Subtask> {
    private final Integer id;
    private final Gson gson;
    private final HTTPTaskManager taskManager;

    public SubtaskHandler(Integer id, Gson gson, HTTPTaskManager taskManager) {
        this.id = id;
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public String getHandler() throws BadRequestException, NotFoundException {
        if(UrlHelpers.checkForQueryIdParam(id)) {
            return gson.toJson(taskManager.getSubtasks());
        } else {
            Task task = taskManager.getSubtaskById(id);

            if (Objects.isNull(task)) {
                throw new NotFoundException();
            }

            return gson.toJson(task);
        }
    }

    @Override
    public void postHandler(String body) throws BadRequestException {
        try {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            taskManager.createSubtask(subtask);
        }
        catch (JsonSyntaxException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public void deleteHandler() throws BadRequestException, NotFoundException {
        if(UrlHelpers.checkForQueryIdParam(id)) {
            taskManager.deleteAllSubtasks();
        } else {
            if (Objects.isNull(taskManager.getSubtaskById(id))) {
                throw new NotFoundException();
            }

            taskManager.deleteSubtask(id);
        }
    }
}
