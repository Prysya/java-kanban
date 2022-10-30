package test.manager;

import main.enums.TaskStatus;
import main.manager.FileBackedTasksManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager();
    }

    private void checkLoadFromFileIsEmpty(File file) {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);

        assertTrue(fileBackedTasksManager.getTasks().isEmpty());
        assertTrue(fileBackedTasksManager.getEpics().isEmpty());
        assertTrue(fileBackedTasksManager.getSubtasks().isEmpty());
    }

    File createFileFromTestFiles(String filename) {
        return Paths.get("src", "test", "historyFiles", filename).toFile();
    }

    @Test
    void shouldReturnClearTaskManagerIfFileIsEmpty() {
        checkLoadFromFileIsEmpty(createFileFromTestFiles("emptyFile.csv"));
    }

    @Test
    void shouldReturnClearTaskManagerIfFileIsNull() {
        checkLoadFromFileIsEmpty(null);
    }

    @Test
    void shouldReturnClearTaskManagerIfFileIsNotDefined() {
        checkLoadFromFileIsEmpty(new File(""));
    }

    @Test
    void shouldReturnClearTaskManagerIfFHistoryIsEmpty() {
        checkLoadFromFileIsEmpty(createFileFromTestFiles("emptyHistory.csv"));
    }

    @Test
    void shouldFillManagerFromTestFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(createFileFromTestFiles("filledHistory.csv"));

        assertEquals(fileBackedTasksManager.getTasks().size(), 2);
        assertEquals(fileBackedTasksManager.getEpics().size(), 2);
        assertEquals(fileBackedTasksManager.getSubtasks().size(), 4);
    }

    @Test
    void shouldUpdateEpicStatusAfterBackup() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(createFileFromTestFiles("filledHistory.csv"));

        assertEquals(fileBackedTasksManager.getEpics().get(0).getTaskStatus(), TaskStatus.DONE);
        assertEquals(fileBackedTasksManager.getEpics().get(1).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldSaveHistory() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.createTask(new Task("test", "test", TaskStatus.NEW, 10, null));
        fileBackedTasksManager.createEpic(new Epic("test", "test"));
        fileBackedTasksManager.createSubtask(new Subtask("test", "test", TaskStatus.NEW, 10, null, fileBackedTasksManager.getEpics().get(0).getId()));

        fileBackedTasksManager.getTaskById(fileBackedTasksManager.getTasks().get(0).getId());
        fileBackedTasksManager.getEpicById(fileBackedTasksManager.getEpics().get(0).getId());
        fileBackedTasksManager.getSubtaskById(fileBackedTasksManager.getSubtasks().get(0).getId());

        FileBackedTasksManager loadedFromHistoryTaskManager = FileBackedTasksManager.loadFromFile(new File("history.csv"));
        assertEquals(loadedFromHistoryTaskManager.getTasks().size(), 1);
        assertEquals(loadedFromHistoryTaskManager.getEpics().size(), 1);
        assertEquals(loadedFromHistoryTaskManager.getSubtasks().size(), 1);
    }
}
