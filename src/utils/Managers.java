package utils;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class Managers {
    private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
