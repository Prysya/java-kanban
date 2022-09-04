import manager.HistoryManager;
import manager.TaskManager;
import utils.Managers;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        for (int i = 1; i < 12; i += 1) {
            /*
            * Случайное число где
            * 1 - Task
            * 2 - Epic
            * 3 - Subtask
            * */
            int randomNumber = 1 + (int) (Math.random() * 2);

            switch (randomNumber) {
                case 1:
                    taskManager.createTask("Задача" + i, "Задача" + i);
                    break;
                case 2:
                    taskManager.createEpic("Эпик" + i, "Эпик" + i);
                    break;
                default:
                    taskManager.createSubtask("Сабтаск" + i, "Сабтаск" + i);
            }
        }

        taskManager.getTasks().forEach(task -> {
            taskManager.getTaskById(task.getId());
            System.out.println(historyManager.getHistory());
        });

        taskManager.getEpics().forEach(epic -> {
            taskManager.getEpicById(epic.getId());
            System.out.println(historyManager.getHistory());
        });

        taskManager.getSubtasks().forEach(subtask -> {
            taskManager.getSubtaskById(subtask.getId());
            System.out.println(historyManager.getHistory());
        });
    }
}
