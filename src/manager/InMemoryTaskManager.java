package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import utils.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    /**
     * Экземпляр класса {@link InMemoryHistoryManager}.
     */
    private final HistoryManager inMemoryHistoryManager =
            Managers.getDefaultHistory();
    /**
     * Мапа({@link Map}) {@link Task}.
     */
    private final Map<Integer, Task> tasks = new HashMap<>();

    /**
     * Мапа({@link Map}) {@link Epic}.
     */
    private final Map<Integer, Epic> epics = new HashMap<>();

    /**
     * Мапа({@link Map}) {@link Subtask}.
     */
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    /**
     * Уникальный Идентификатор задач.
     */
    private int id = 0;

    /**
     * Получение экземпляра менеджера истории.
     * @return {@link HistoryManager}
     */
    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int generateId() {
        id += 1;
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getTasks() {
        return (new ArrayList<>(tasks.values()));
    }

    /**
     * {@inheritDoc}
     */
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllTasks() {
        tasks.forEach((taskId, task) ->  inMemoryHistoryManager.remove(taskId));
        tasks.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();

        epics.forEach((epicId, epic) -> inMemoryHistoryManager.remove(epicId));
        epics.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllSubtasks() {
        subtasks.forEach((subtaskId, subtask) ->
                inMemoryHistoryManager.remove(subtaskId));
        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.deleteAllSubtaskIds();
            epic.updateStatus();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task getTaskById(final int taskId) {
        Task task = tasks.get(taskId);

        if (task != null) {
            add(task);
        }

        return tasks.get(taskId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Epic getEpicById(final int epicId) {
        Epic epic = epics.get(epicId);

        if (epic != null) {
            add(epic);
        }
        return epic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subtask getSubtaskById(final int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

        if (subtask != null) {
            add(subtask);
        }

        return subtask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createTask(final Task task) {
        int taskId = generateId();
        task.setId(taskId);

        tasks.put(taskId, task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createEpic(final Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);

        epics.put(epicId, epic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSubtask(final Subtask subtask) {
        Epic epic = epics.get(subtask.getParentEpicId());

        if (epic != null) {
            int subtaskId = generateId();

            subtask.setId(subtaskId);

            epic.addSubtaskId(subtaskId);
            subtasks.put(id, subtask);

            updateEpicStatus(epic);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTask(final Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEpic(final Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSubtask(final Subtask subtask) {
        Epic epic = epics.get(subtask.getParentEpicId());

        if (epic != null) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(final int taskId) {
        tasks.remove(taskId);
        inMemoryHistoryManager.remove(taskId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEpic(final int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.remove(epicId);
            inMemoryHistoryManager.remove(epicId);

            epic.getSubtasks(subtasks).forEach(subtask -> {
                int subtaskId = subtask.getId();
                subtasks.remove(subtaskId);
                inMemoryHistoryManager.remove(subtaskId);
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSubtask(final int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Epic epic = epics.get(subtasks.get(subtaskId).getParentEpicId());

            epic.deleteSubtaskId(subtaskId);
            updateEpicStatus(epic);

            subtasks.remove(subtaskId);
            inMemoryHistoryManager.remove(subtaskId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Subtask> getEpicSubtasks(final Epic epic) {
        return epic.getSubtasks(subtasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEpicStatus(final Epic epic) {
        epic.updateStatus(epic.getSubtasks(subtasks));
    }

    /**
     * {@link InMemoryHistoryManager#getHistory}.
     * @return список {@link Task}, {@link Epic}, {@link Subtask}
     */
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    /**
     * Добавление {@link Task}, {@link Epic}, {@link Subtask} в список истории.
     *
     * @param task {@link Task}, {@link Epic}, {@link Subtask}
     */
    private void add(final Task task) {
        inMemoryHistoryManager.add(task);
    }
}
