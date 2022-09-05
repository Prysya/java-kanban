package utils;

import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class Managers {
    private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryTaskManager;
    }
}
