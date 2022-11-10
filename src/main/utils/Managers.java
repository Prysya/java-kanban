package main.utils;

import main.manager.FileBackedTasksManager;
import main.manager.HTTPTaskManager;
import main.manager.InMemoryHistoryManager;

public final class Managers {
    private Managers() {
    }

    public static HTTPTaskManager getDefault() {
        return new HTTPTaskManager("http://localhost:8078");
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return new FileBackedTasksManager();
    }
}
