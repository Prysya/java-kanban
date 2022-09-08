package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int HISTORY_MAX_SIZE = 10;
    /**
     * Лист с историей всех задач({@link Task}, {@link Epic}, {@link Subtask})
     */
    private final List<Task> history = new LinkedList<>();

    /**
     * Получение списка последних просмотренных {@link #history}
     */
    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
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
    private void updateHistorySize() {
        if (history.size() > HISTORY_MAX_SIZE) {
            history.remove(0);
        }
    }
}
