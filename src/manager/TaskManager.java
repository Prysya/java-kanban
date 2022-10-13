package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    /**
     * Метод для генерации уникальному модификатору.
     *
     * @return уникальному модификатору
     */
    int generateId();

    /**
     * Получение списка задач.
     *
     * @return список {@link Task}
     */
    List<Task> getTasks();

    /**
     * Получение списка эпиков.
     *
     * @return список {@link Epic}
     */
    List<Epic> getEpics();

    /**
     * Получение списка подзадач.
     *
     * @return {@link Subtask}
     */
    List<Subtask> getSubtasks();

    /**
     * Удаление всех {@link Task}.
     */
    void deleteAllTasks();

    /**
     * Удаление всех {@link Epic}.
     */
    void deleteAllEpics();

    /**
     * Удаление всех {@link Subtask}.
     */
    void deleteAllSubtasks();

    /**
     * Получение {@link Task} по уникальному модификатору.
     *
     * @param id уникальный идентификатор
     * @return {@link Task}
     */
    Task getTaskById(int id);

    /**
     * Получение {@link Epic} по уникальному модификатору.
     *
     * @param id уникальный идентификатор
     * @return {@link Epic}
     */
    Epic getEpicById(int id);

    /**
     * Получение {@link Subtask} по уникальному модификатору.
     *
     * @param id уникальный идентификатор
     * @return {@link Subtask}
     */
    Subtask getSubtaskById(int id);

    /**
     * Создание {@link Task}.
     *
     * @param task {@link Task}
     */
    void createTask(Task task);

    /**
     * Создание {@link Epic}.
     *
     * @param epic {@link Epic}
     */
    void createEpic(Epic epic);

    /**
     * Создание {@link Subtask}.
     *
     * @param subtask {@link Subtask}
     */
    void createSubtask(Subtask subtask);

    /**
     * Обновление {@link Task}.
     *
     * @param task {@link Task}
     */
    void updateTask(Task task);

    /**
     * Обновление {@link Epic}.
     *
     * @param epic {@link Epic}
     */
    void updateEpic(Epic epic);

    /**
     * Обновление {@link Subtask}.
     *
     * @param subtask {@link Subtask}
     */
    void updateSubtask(Subtask subtask);

    /**
     * Удаление {@link Task}.
     *
     * @param id уникальный идентификатор
     */
    void deleteTask(int id);

    /**
     * Удаление {@link Epic}.
     *
     * @param id уникальный идентификатор
     */
    void deleteEpic(int id);

    /**
     * Удаление {@link Subtask}.
     *
     * @param id уникальный идентификатор
     */
    void deleteSubtask(int id);

    /**
     * Получение листа уникальных идентификаторов {@link Subtask} определенного {@link Epic}.
     *
     * @param epic {@link Epic}
     * @return список {@link Epic}
     */
    List<Subtask> getEpicSubtasks(Epic epic);

    /**
     * Обновление статуса эпика.
     *
     * @param epic {@link Epic}
     */
    void updateEpicStatus(Epic epic);
}
