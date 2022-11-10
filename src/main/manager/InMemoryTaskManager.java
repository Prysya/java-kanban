package main.manager;

import main.enums.TaskStatus;
import main.exceptions.SameDateException;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.utils.CustomComparators;
import main.utils.Managers;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager inMemoryHistoryManager =
        Managers.getDefaultHistory();
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final TreeSet<Task> tasksTree = new TreeSet<>(
        CustomComparators.byStartTime()
    );
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
        if (Objects.isNull(epic)) return;

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
        if (Objects.isNull(epic)) return;

        List<Integer> ids = epic.getSubtaskIds();
        ids.remove(Integer.valueOf(id));

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
        tasks.forEach((taskId, task) -> {
            inMemoryHistoryManager.remove(taskId);
            tasksTree.remove(task);
        });
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
        subtasks.forEach((subtaskId, subtask) -> {
            inMemoryHistoryManager.remove(subtaskId);
            tasksTree.remove(subtask);
        });
        subtasks.clear();

        epics.values().forEach(epic -> {
            deleteAllEpicSubtaskIds(epic);
            updateEpicStatus(epic);
            updateEpicTimeAndDutation(epic);
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
        if (Objects.isNull(task)) {
            return;
        }

        int taskId = generateId();
        task.setId(taskId);

        try {
            addTaskToTree(task);
            tasks.put(taskId, task);
        } catch (SameDateException ignored) {
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (Objects.isNull(epic)) {
            return;
        }

        int epicId = generateId();
        epic.setId(epicId);

        epics.put(epicId, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (Objects.isNull(subtask)) {
            return;
        }

        Epic epic = epics.get(subtask.getParentEpicId());

        if (epic != null) {
            int subtaskId = generateId();

            subtask.setId(subtaskId);

            try {
                addTaskToTree(subtask);
                addEpicSubtaskId(epic, subtaskId);
                subtasks.put(id, subtask);

                updateEpicStatus(epic);
                updateEpicTimeAndDutation(epic);
            } catch (SameDateException ignored) {
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        if (Objects.isNull(task)) {
            return;
        }

        try {
            addTaskToTree(task);
            tasks.put(task.getId(), task);
        } catch (SameDateException ignored) {
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (Objects.isNull(epic)) {
            return;
        }

        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (Objects.isNull(subtask)) {
            return;
        }

        Epic epic = epics.get(subtask.getParentEpicId());

        if (epic != null) {
            try {
                addTaskToTree(subtask);
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(epic);
                updateEpicTimeAndDutation(epic);
            } catch (SameDateException ignored) {
            }

        }
    }

    @Override
    public void deleteTask(int taskId) {
        tasksTree.remove(tasks.get(taskId));
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
            updateEpicTimeAndDutation(epic);

            tasksTree.remove(subtasks.get(subtaskId));
            subtasks.remove(subtaskId);
            inMemoryHistoryManager.remove(subtaskId);
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(Epic epic) {
        List<Subtask> epicSubtasksList = new ArrayList<>();

        if (Objects.isNull(epic)) return epicSubtasksList;

        for (int id : epic.getSubtaskIds()) {
            if (subtasks.containsKey(id)) {
                epicSubtasksList.add(subtasks.get(id));
            }
        }

        return epicSubtasksList;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        if (Objects.isNull(epic)) return;

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

    @Override
    public void updateEpicTimeAndDutation(Epic epic) {
        if (Objects.isNull(epic)) return;

        List<Integer> ids = epic.getSubtaskIds();

        if (ids.isEmpty()) {
            epic.setEndTime(null);
            epic.setDuration(0);
            return;
        }

        List<Subtask> subtasksList = getEpicSubtasks(epic);

        int duration = 0;
        LocalDateTime startTime = null;

        for (Subtask subtask : subtasksList) {
            duration += subtask.getDuration();
            if (Objects.isNull(startTime) || (!Objects.isNull(subtask.getStartTime()) && startTime.isAfter(subtask.getStartTime()))) {
                startTime = subtask.getStartTime();
            }
        }

        epic.setDuration(duration);
        epic.setStartTime(startTime);
        epic.setEndTime(startTime != null ? startTime.plusMinutes(duration) : null);
    }

    /**
     * @return список задач отсортированных по времени начала
     */
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksTree);
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

    /**
     * Получение экземпляра менеджера истории.
     *
     * @return {@link HistoryManager}
     */
    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
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

    /**
     * Добавление {@link Task}, {@link Epic}, {@link Subtask} в {@link #tasksTree}.
     *
     * @throws SameDateException при пересечении времени задач
     */
    private void addTaskToTree(Task task) throws SameDateException {
        if (tasksTree.contains(task)) {
            throw new SameDateException("Ошибка пересечения задач");
        }

        tasksTree.add(task);
    }
}
