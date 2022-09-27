package manager;

import lib.CustomTaskLinkedList;
import lib.Node;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    /**
     * Лист с историей всех задач({@link Task}, {@link Epic}, {@link Subtask})
     */
    private final CustomTaskLinkedList history = new CustomTaskLinkedList();

    /**
     * Получение списка последних просмотренных {@link #history}
     */
    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    /**
     * Добавление {@link Task} в лист {@link #history}
     */
    @Override
    public void add(Task task) {
        remove(task.getId());

        history.linkLast(task);
    }

    /**
     * Удаление {@link Task} из {@link #history}
     * @param id уникальный идентификатор задачи
     */
    @Override
    public void remove(int id) {
        Node<Task> node = history.get(id);

        if (node != null) {
            history.removeNode(node);
        }
    }

}
