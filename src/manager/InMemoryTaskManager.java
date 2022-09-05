package manager;

import enums.TaskStatus;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager extends InMemoryHistoryManager implements TaskManager {
    /**
     * Хэш мапа {@link Task}
     */
    private final static HashMap<Integer, Task> tasks = new HashMap<>();

    /**
     * Хэш мапа {@link Epic}
     */
    private final static HashMap<Integer, Epic> epics = new HashMap<>();

    /**
     * Хэш мапа {@link Subtask}
     */
    private final static HashMap<Integer, Subtask> subtasks = new HashMap<>();

    /**
     * Уникальный Идентификатор задач
     */
    private int id = 0;

    /**
     * Метод для генерации {@link #id}
     *
     * @return {@link #id}
     */
    @Override
    public int generateId() {
        id += 1;
        return id;
    }

    /**
     * Получение списка {@link #tasks}
     */
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(tasks.values());
    }

    /**
     * Получение списка {@link #epics}
     */
    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    /**
     * Получение списка {@link #subtasks}
     */
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtasks.values());
    }

    /**
     * Удаление всех {@link Task}
     */
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    /**
     * Удаление всех {@link Epic}
     */
    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    /**
     * Удаление всех {@link Subtask}
     */
    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    /**
     * Получение {@link Task} по {@link #id}
     */
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);

        if (task != null) {
            add(task);
        }

        return tasks.get(id);
    }

    /**
     * Получение {@link Epic} по {@link #id}
     */
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);

        if (epic != null) {
            add(epic);
        }
        return epic;
    }

    /**
     * Получение {@link Subtask} по {@link #id}
     */
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);

        if (subtask != null) {
            add(subtask);
        }

        return subtask;
    }

    /**
     * Создание {@link Task}
     */
    @Override
    public void createTask(String title, String description) {
        int taskId = generateId();
        tasks.put(taskId, new Task(title, description, taskId, TaskStatus.NEW));
    }

    /**
     * Создание {@link Epic}
     */
    @Override
    public void createEpic(String title, String description) {
        int epicId = generateId();
        epics.put(epicId, new Epic(title, description, epicId));
    }

    /**
     * Создание {@link Subtask}
     */
    @Override
    public void createSubtask(String title, String description) {
        int subtaskId = generateId();
        subtasks.put(id, new Subtask(title, description, subtaskId, TaskStatus.NEW));
    }

    /**
     * Обновление {@link Task}
     */
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * Обновление {@link Epic}
     */
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /**
     * Обновление {@link Subtask}
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        tasks.put(subtask.getId(), subtask);
    }

    /**
     * Удаление {@link Task}
     */
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    /**
     * Удаление {@link Epic}
     */
    @Override
    public void deleteEpic(int id) {
        epics.remove(id);
    }

    /**
     * Удаление {@link Subtask}
     */
    @Override
    public void deleteSubtask(int id) {
        subtasks.remove(id);
    }

    /**
     * Получение списка {@link Subtask} определенного {@link Epic}
     */
    @Override
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    /**
     * Обновление статуса эпика
     */
    @Override
    public void updateEpicStatus(Epic epic) {
        epic.updateStatus();
    }
}
