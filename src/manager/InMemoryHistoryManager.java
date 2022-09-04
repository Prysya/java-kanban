package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    /**
     * Лист с историей всех задач({@link Task}, {@link Epic}, {@link Subtask})
     */
    List<Task> history = new ArrayList<>();

    /**
     * Получение списка последних 10 просмотренных {@link #history}
     */
    @Override
    public List<Task> getHistory() {
        return history;
    }

    /**
     * Добавление {@link Task} в лист {@link #history}
     */
    @Override
    public void add(Task task) {
        history.add(task);
        updateHistorySize();
    }

    /**
     * Проверка на величину листа {@link #history} и удаление первого элемента
     */
    void updateHistorySize() {
        if (history.size() > 10) {
            history.remove(0);
        }
    }
}
