package main.serverHandlers;

import com.google.gson.Gson;
import main.exceptions.MethodNotAllowedException;
import main.manager.HTTPTaskManager;
import main.task.Task;

public class HistoryHandler implements ServerHandler<Task> {
    private final Gson gson;
    private final HTTPTaskManager taskManager;

    public HistoryHandler(Gson gson, HTTPTaskManager taskManager) {
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public String getHandler() {
        return gson.toJson(taskManager.getHistory());
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
