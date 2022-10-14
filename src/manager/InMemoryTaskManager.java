package manager;

import enums.TaskStatus;
import task.Epic;
import task.Subtask;
import task.Task;
import utils.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager inMemoryHistoryManager =
            Managers.getDefaultHistory();
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    /**
     * Уникальный Идентификатор задач.
     */
    private int id = 0;

    /**
     * Добавление уникального идентификатора подзадачи в лист подзадач.
     *
     * @param epic {@link Epic}
     * @param id   уникальный идентификатор подадачи({@link Subtask})
     */
    private static void addEpicSubtaskId(Epic epic, int id) {
        if (epic == null) return;

        List<Integer> ids = epic.getSubtaskIds();
        ids.add(id);

        epic.setSubtaskIds(ids);
    }

    /**
     * Удаление идентификатора подзадачи из списка идентификаторов подадач эпика.
     *
     * @param epic {@link Epic}
     * @param id   уникальный идентификатор подадачи({@link Subtask})
     */
    private static void deleteEpicSubtaskId(Epic epic, int id) {
        if (epic == null) return;

        List<Integer> ids = epic.getSubtaskIds();
        ids.remove(id);

        epic.setSubtaskIds(ids);
    }

    /**
     * Удаление всех идентификаторов подзадач подзадачи из эпика.
     *
     * @param epic {@link Epic}
     */
    private static void deleteAllEpicSubtaskIds(Epic epic) {
        epic.setSubtaskIds(new ArrayList<>());
    }

    /**
     * Получение экземпляра менеджера истории.
     *
     * @return {@link HistoryManager}
     */
    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

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
        tasks.forEach((taskId, task) -> inMemoryHistoryManager.remove(taskId));
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();

        epics.forEach((epicId, epic) -> inMemoryHistoryManager.remove(epicId));
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.forEach((subtaskId, subtask) ->
                inMemoryHistoryManager.remove(subtaskId));
        subtasks.clear();

        epics.values().forEach(epic -> {
            deleteAllEpicSubtaskIds(epic);
            updateEpicStatus(epic);
        });
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);

        if (task != null) {
            add(task);
        }

        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic != null) {
            add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

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

            addEpicSubtaskId(epic, subtaskId);
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
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        inMemoryHistoryManager.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.remove(epicId);
            inMemoryHistoryManager.remove(epicId);

            getEpicSubtasks(epic).forEach(subtask -> {
                int subtaskId = subtask.getId();
                subtasks.remove(subtaskId);
                inMemoryHistoryManager.remove(subtaskId);
            });
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Epic epic = epics.get(subtasks.get(subtaskId).getParentEpicId());

            deleteEpicSubtaskId(epic, subtaskId);
            updateEpicStatus(epic);

            subtasks.remove(subtaskId);
            inMemoryHistoryManager.remove(subtaskId);
        }
    }

    /**
     * {@link InMemoryHistoryManager#getHistory}.
     *
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
    private void add(Task task) {
        inMemoryHistoryManager.add(task);
    }

    @Override
    public List<Subtask> getEpicSubtasks(Epic epic) {
        List<Subtask> epicSubtasksList = new ArrayList<>();

        if (epic == null) return epicSubtasksList;

        for (int id : epic.getSubtaskIds()) {
            if (subtasks.containsKey(id)) {
                epicSubtasksList.add(subtasks.get(id));
            }
        }

        return epicSubtasksList;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        if (epic == null) return;

        List<Integer> ids = epic.getSubtaskIds();

        if (ids.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        List<Subtask> subtasksList = getEpicSubtasks(epic);

        int newCount = calculateTasksCount(subtasksList, TaskStatus.NEW);
        int doneCount = calculateTasksCount(subtasksList, TaskStatus.DONE);

        if (newCount == ids.size()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (doneCount == ids.size()) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    /**
     * Расчет количества подзадач по статусу
     *
     * @param subtaskList лист подзадач
     * @param status      статус для фильтрации
     * @return возвращает количество отфильтрованных задач
     */
    private int calculateTasksCount(List<Subtask> subtaskList, TaskStatus status) {
        return (int) subtaskList.stream()
                .filter(subtask ->
                        subtask != null && subtask.getTaskStatus()
                                .equals(status)).count();
    }
}
