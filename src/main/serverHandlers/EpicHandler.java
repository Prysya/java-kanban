package main.serverHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.exceptions.BadRequestException;
import main.exceptions.NotFoundException;
import main.manager.HTTPTaskManager;
import main.task.Epic;
import main.utils.UrlHelpers;

import java.util.Objects;

public class EpicHandler implements ServerHandler<Epic> {
    private final Integer id;
    private final Gson gson;
    private final HTTPTaskManager taskManager;

    public EpicHandler(Integer id, Gson gson, HTTPTaskManager taskManager) {
        this.id = id;
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public String getHandler() throws BadRequestException, NotFoundException {
        if (UrlHelpers.checkForQueryIdParam(id)) {
            return gson.toJson(taskManager.getEpics());
        } else {
            Epic epic = taskManager.getEpicById(id);

            if (Objects.isNull(epic)) {
                throw new NotFoundException();
            }

            return gson.toJson(epic);
        }
    }

    @Override
    public void postHandler(String body) throws BadRequestException {
        try {
            Epic epic = gson.fromJson(body, Epic.class);
            taskManager.createEpic(new Epic(epic.getTitle(), epic.getDescription()));
        }
        catch (JsonSyntaxException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public void deleteHandler() throws BadRequestException, NotFoundException {
        if (UrlHelpers.checkForQueryIdParam(id)) {
            taskManager.deleteAllEpics();
        } else {
            if (Objects.isNull(taskManager.getEpicById(id))) {
                throw new NotFoundException();
            }

            taskManager.deleteEpic(id);
        }
    }
}
