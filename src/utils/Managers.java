package utils;

import manager.FileBackedTasksManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;

import java.io.File;

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
