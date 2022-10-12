package manager;

import lib.CustomTaskLinkedList;
import lib.Node;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    /**
     * Лист с историей всех задач({@link Task}, {@link Epic}, {@link Subtask}).
     */
    private final CustomTaskLinkedList history = new CustomTaskLinkedList();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final Task task) {
        remove(task.getId());

        history.linkLast(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final int id) {
        Node<Task> node = history.get(id);

        if (node != null) {
            history.removeNode(node);
        }
    }
}
