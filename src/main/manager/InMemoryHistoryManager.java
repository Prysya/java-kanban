package main.manager;

import main.lib.CustomTaskLinkedList;
import main.lib.Node;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    /**
     * Лист с историей всех задач({@link Task}, {@link Epic}, {@link Subtask}).
     */
    private final CustomTaskLinkedList history = new CustomTaskLinkedList();

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void add(Task task) {
        if (Objects.isNull(task)) return;

        remove(task.getId());

        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = history.get(id);

        if (node != null) {
            history.removeNode(node);
        }
    }
}
