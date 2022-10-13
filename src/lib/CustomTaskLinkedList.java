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
        if (task == null) return;

        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);

        tail = newNode;
        if (head == null) {
            head = newNode;
        }

        if (oldTail != null) {
            oldTail.setNext(newNode);
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
                tasks.add(currentNode.getData());

                currentNode = currentNode.getNext();
            }
        }

        return tasks;
    }

    /**
     * Удаляет ноду из списка.
     *
     * @param node {@link Node}
     */
    public void removeNode(Node<Task> node) {
        if (node == null) return;

        hashTable.remove(node.getData().getId());

        if (head == node) {
            head = node.getNext();
        }

        if (tail == node) {
            tail = node.getPrev();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        }

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
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
