package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {
    /**
     * Получение списка последних просмотренных задач
     */
    List<Task> getHistory();

    /**
     * Добавление {@link Task} в историю задач
     */
    void add(Task task);

    /**
     * Удаление {@link Task} из истории задач
     *
     * @param id уникальный идентификатор задачи
     */
    void remove(int id);
}
