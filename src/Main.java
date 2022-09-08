import enums.TaskStatus;
import manager.InMemoryTaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import utils.Managers;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = Managers.getDefault();

        taskManager.createTask(new Task("Таск 1", "Таск 1", TaskStatus.NEW));
        taskManager.createTask(new Task("Таск 2", "Таск 2", TaskStatus.NEW));
        taskManager.createTask(new Task("Таск 3", "Таск 3", TaskStatus.NEW));

        taskManager.createEpic(new Epic("Эпик 1", "Эпик 1"));
        taskManager.createEpic(new Epic("Эпик 2", "Эпик 2"));
        taskManager.createEpic(new Epic("Эпик 3", "Эпик 3"));

        taskManager.createSubtask(new Subtask("Подтаска 1", "Подтаска 1 - Epic 1", TaskStatus.DONE, 4));
        taskManager.createSubtask(new Subtask("Подтаска 2", "Подтаска 2 - Epic 1", TaskStatus.DONE, 4));
        taskManager.createSubtask(new Subtask("Подтаска 3", "Подтаска 3 - Epic 1", TaskStatus.DONE, 4));

        taskManager.createSubtask(new Subtask("Подтаска 4", "Подтаска 4 - Epic 2", TaskStatus.IN_PROGRESS, 5));
        taskManager.createSubtask(new Subtask("Подтаска 5", "Подтаска 5 - Epic 2", TaskStatus.IN_PROGRESS, 5));
        taskManager.createSubtask(new Subtask("Подтаска 6", "Подтаска 6 - Epic 2", TaskStatus.IN_PROGRESS, 5));

        taskManager.createSubtask(new Subtask("Подтаска 7", "Подтаска 7 - Epic 3", TaskStatus.NEW, 6));
        taskManager.createSubtask(new Subtask("Подтаска 8", "Подтаска 8 - Epic 3", TaskStatus.IN_PROGRESS, 6));
        taskManager.createSubtask(new Subtask("Подтаска 9", "Подтаска 9 - Epic 3", TaskStatus.DONE, 6));

        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        tasks.forEach(task -> {
            taskManager.getTaskById(task.getId());
            System.out.println(taskManager.getHistory());
        });

        epics.forEach(epic -> {
            taskManager.getEpicById(epic.getId());
            System.out.println(taskManager.getHistory());
        });

        subtasks.forEach(subtask -> {
            taskManager.getSubtaskById(subtask.getId());
            System.out.println(taskManager.getHistory());
        });
    }
}
