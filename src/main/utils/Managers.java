package main.utils;

import main.manager.FileBackedTasksManager;
import main.manager.InMemoryHistoryManager;
import main.manager.InMemoryTaskManager;

public final class Managers {
    private Managers() {
    }

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return new FileBackedTasksManager();
    }
}
