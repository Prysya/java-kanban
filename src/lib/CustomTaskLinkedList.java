package lib;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomTaskLinkedList {
    /**
     * Хэш таблица
     */
    private final Map<Integer, Node<Task>> hashTable = new HashMap<>();
    /**
     * Указатель на первый элемент списка. Он же first
     */
    private Node<Task> head;
    /**
     * Указатель на последний элемент списка. Он же last
     */
    private Node<Task> tail;

    /**
     * @param task {@link Task}
     *             Добавление элемента в конец списка
     */
    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);

        tail = newNode;
        if (head == null) {
            head = newNode;
        }

        if (oldTail != null) {
            oldTail.next = newNode;
        }

        hashTable.put(task.getId(), newNode);
    }

    /**
     * Получение задач в порядке списка
     */
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        if (head != null) {
            Node<Task> currentNode = head;

            while (currentNode != null) {
                tasks.add(currentNode.data);

                currentNode = currentNode.next;
            }
        }

        return tasks;
    }

    /**
     * @param node {@link Node}
     * Удаляет ноду из списка
     */
    public void removeNode(Node<Task> node) {
        hashTable.remove(node.data.getId());

        if (head == node) {
            head = node.next;
        }

        if (tail == node) {
            tail = node.prev;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        }
    }

    /**
     * Получение первого элемента в списке
     *
     * @return {@link Node}
     */
    public Node<Task> getHead() {
        return head;
    }

    /**
     * @return количество элементов в списке
     */
    public int size() {
        return hashTable.size();
    }

    /**
     * @param id уникальный идентификатор задачи
     * @return возвращает ноду из хеш таблицы
     */
    public Node<Task> get(int id) {
        return hashTable.get(id);
    }
}
