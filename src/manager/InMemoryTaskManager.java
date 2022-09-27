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
    public List<Task> getTasks() {
        return (new ArrayList<>(tasks.values()));
    }

    /**
     * Получение списка {@link #epics}
     */
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    /**
     * Получение списка {@link #subtasks}
     */
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
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
        deleteAllSubtasks();

        epics.clear();
    }

    /**
     * Удаление всех {@link Subtask}
     */
    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.deleteAllSubtaskIds();
            epic.updateStatus();
        });
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
    public void createTask(Task task) {
        int taskId = generateId();
        task.setId(taskId);

        tasks.put(taskId, task);
    }

    /**
     * Создание {@link Epic}
     */
    @Override
    public void createEpic(Epic epic) {
        int epicId = generateId();
        epic.setId(epicId);

        epics.put(epicId, epic);
    }

    /**
     * Создание {@link Subtask}
     */
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
        Epic epic = epics.get(subtask.getParentEpicId());

        if (epic != null) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
        }
    }

    /**
     * Удаление {@link Task}
     */
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    /**
     * Удаление {@link Epic}
     */
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

    /**
     * Удаление {@link Subtask}
     */
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

    /**
     * Получение листа уникальных идентификаторов {@link Subtask} определенного {@link Epic}
     */
    @Override
    public List<Subtask> getEpicSubtasks(Epic epic ) {
        return epic.getSubtasks(subtasks);
    }

    /**
     * Обновление статуса эпика
     */
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
