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

        taskManager.createEpic(new Epic("Эпик 1", "Эпик 1"));
        taskManager.createEpic(new Epic("Эпик 2", "Эпик 2"));

        taskManager.createSubtask(new Subtask("Подтаска 1", "Подтаска 1 - Epic 1", TaskStatus.DONE, 3));
        taskManager.createSubtask(new Subtask("Подтаска 2", "Подтаска 2 - Epic 1", TaskStatus.DONE, 3));
        taskManager.createSubtask(new Subtask("Подтаска 3", "Подтаска 3 - Epic 1", TaskStatus.DONE, 3));
        taskManager.createSubtask(new Subtask("Подтаска 4", "Подтаска 4 - Epic 1", TaskStatus.DONE, 3));


        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        tasks.forEach(task -> {
            taskManager.getTaskById(task.getId());
        });

        epics.forEach(epic -> {
            taskManager.getEpicById(epic.getId());
        });

        subtasks.forEach(subtask -> {
            taskManager.getSubtaskById(subtask.getId());
        });

        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistory());
        taskManager.deleteTask(2);
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpic(3);
        System.out.println(taskManager.getHistory());
    }
}
