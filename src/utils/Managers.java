package utils;

import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class Managers {
    static InMemoryTaskManager InMemoryTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return InMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return InMemoryTaskManager;
    }
}
