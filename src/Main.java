import enums.TaskStatus;
import manager.Manager;
import task.Epic;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Задача 1", "Собрать коробки", Manager.generateId(), TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Упаковать кошку", Manager.generateId(), TaskStatus.NEW);

        Epic epic1 = new Epic("Эпик1", "Эпик 1", Manager.generateId());
        Epic epic2 = new Epic("Эпик2", "Эпик 2", Manager.generateId());

        Subtask subtask1 = new Subtask("Сабтаск1", "Сабтаск1", Manager.generateId(), TaskStatus.DONE);
        Subtask subtask2 = new Subtask("Сабтаск2", "Сабтаск2", Manager.generateId(), TaskStatus.IN_PROGRESS);
        Subtask subtask3 = new Subtask("Сабтаск3", "Сабтаск3", Manager.generateId(), TaskStatus.DONE);

        epic1.addSubtask(subtask1);
        epic1.addSubtask(subtask2);
        epic2.addSubtask(subtask3);

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
    }
}
