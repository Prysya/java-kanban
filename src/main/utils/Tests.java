package main.utils;

import main.enums.TaskStatus;
import main.manager.FileBackedTasksManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public final class Tests {
    private Tests() {
    }

    public static void generateTests() {
        FileBackedTasksManager taskManager = Managers.getDefaultFileBacked();

        taskManager.createTask(new Task("Таск 1", "Таск 1", TaskStatus.NEW, 10, LocalDateTime.now().minusDays(30)));
        taskManager.createTask(new Task("Таск 2", "Таск 2", TaskStatus.NEW, 10, LocalDateTime.now().minusDays(2)));

        taskManager.createEpic(new Epic("Эпик 1", "Эпик 1"));
        taskManager.createEpic(new Epic("Эпик 2", "Эпик 2"));

        for (int i = 1; i < 5; i += 1) {
            taskManager.createSubtask(new Subtask("Подтаска " + i, "Подтаска " + i + " - Epic " + i, TaskStatus.DONE, 10, LocalDateTime.now().minusDays(i), 3));
        }


        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        tasks.forEach(task -> taskManager.getTaskById(task.getId()));
        epics.forEach(epic -> taskManager.getEpicById(epic.getId()));
        subtasks.forEach(subtask -> taskManager.getSubtaskById(subtask.getId()));

        taskManager.getTaskById(1);

        FileBackedTasksManager fileBackedTasksManager =
                FileBackedTasksManager.loadFromFile(new File("history.csv"));

        System.out.println(fileBackedTasksManager.getTasks());
        System.out.println(fileBackedTasksManager.getEpics());
        System.out.println(fileBackedTasksManager.getSubtasks());
        System.out.println(fileBackedTasksManager.getHistory());
    }
}
