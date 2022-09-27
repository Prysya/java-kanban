package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    /**
     * Метод для генерации уникальному модификатору
     *
     * @return уникальному модификатору
     */
    int generateId();

    /**
     * Получение списка задач
     */
    List<Task> getTasks();

    /**
     * Получение списка эпиков
     */
    List<Epic> getEpics();

    /**
     * Получение списка подзадач
     */
    List<Subtask> getSubtasks();

    /**
     * Удаление всех {@link Task}
     */
    void deleteAllTasks();

    /**
     * Удаление всех {@link Epic}
     */
    void deleteAllEpics();

    /**
     * Удаление всех {@link Subtask}
     */
    void deleteAllSubtasks();

    /**
     * Получение {@link Task} по уникальному модификатору
     */
    Task getTaskById(int id);

    /**
     * Получение {@link Epic} по уникальному модификатору
     */
    Epic getEpicById(int id);

    /**
     * Получение {@link Subtask} по уникальному модификатору
     */
    Subtask getSubtaskById(int id);

    /**
     * Создание {@link Task}
     */
    void createTask(Task task);

    /**
     * Создание {@link Epic}
     */
    void createEpic(Epic epic);

    /**
     * Создание {@link Subtask}
     */
    void createSubtask(Subtask subtask);

    /**
     * Обновление {@link Task}
     */
    void updateTask(Task task);

    /**
     * Обновление {@link Epic}
     */
    void updateEpic(Epic epic);

    /**
     * Обновление {@link Subtask}
     */
    void updateSubtask(Subtask subtask);

    /**
     * Удаление {@link Task}
     */
    void deleteTask(int id);

    /**
     * Удаление {@link Epic}
     */
    void deleteEpic(int id);

    /**
     * Удаление {@link Subtask}
     */
    void deleteSubtask(int id);

    /**
     * Получение листа уникальных идентификаторов {@link Subtask} определенного {@link Epic}
     */
    List<Subtask> getEpicSubtasks(Epic epic);

    /**
     * Обновление статуса эпика
     */
    void updateEpicStatus(Epic epic);
}
