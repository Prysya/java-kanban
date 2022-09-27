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
     * Экземпляр класса {@link InMemoryHistoryManager}
     */
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    /**
     * Мапа({@link Map}) {@link Task}
     */
    private final Map<Integer, Task> tasks = new HashMap<>();

    /**
     * Мапа({@link Map}) {@link Epic}
     */
    private final Map<Integer, Epic> epics = new HashMap<>();

    /**
     * Мапа({@link Map}) {@link Subtask}
     */
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    /**
     * Уникальный Идентификатор задач
     */
    private int id = 0;


    @Override
    public int generateId() {
        id += 1;
        return id;
    }


    @Override
    public List<Task> getTasks() {
        return (new ArrayList<>(tasks.values()));
    }


    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }


    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();

        epics.clear();
    }


    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.deleteAllSubtaskIds();
            epic.updateStatus();
        });
    }


    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);

        if (task != null) {
            add(task);
        }

        return tasks.get(id);
    }


    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);

        if (epic != null) {
            add(epic);
        }
        return epic;
    }


    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);

        if (subtask != null) {
            add(subtask);
        }

        return subtask;
    }


    @Override
    public void createTask(Task task) {
        int taskId = generateId();
        task.setId(taskId);

        tasks.put(taskId, task);
    }


    @Override
    public void createEpic(Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);

        epics.put(epicId, epic);
    }


    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getParentEpicId());

        if (epic != null) {
            int subtaskId = generateId();

            subtask.setId(subtaskId);

            epic.addSubtaskId(subtaskId);
            subtasks.put(id, subtask);

            updateEpicStatus(epic);
        }
    }


    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }


    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getParentEpicId());

        if (epic != null) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
        }
    }


    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }


    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            inMemoryHistoryManager.remove(id);

            epic.getSubtasks(subtasks).forEach(subtask -> {
                int subtaskId = subtask.getId();
                subtasks.remove(subtaskId);
                inMemoryHistoryManager.remove(subtaskId);
            });
        }
    }


    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getParentEpicId());

            epic.deleteSubtaskId(id);
            updateEpicStatus(epic);

            subtasks.remove(id);
            inMemoryHistoryManager.remove(id);
        }
    }


    @Override
    public List<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks(subtasks);
    }


    @Override
    public void updateEpicStatus(Epic epic) {
        epic.updateStatus(epic.getSubtasks(subtasks));
    }

    /**
     * {@link InMemoryHistoryManager#getHistory}
     */
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    /**
     * Добавление {@link Task}, {@link Epic}, {@link Subtask} в список истории
     */
    private void add(Task task) {
        inMemoryHistoryManager.add(task);
    }
}
