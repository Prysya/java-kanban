package manager;

import enums.TaskStatus;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    /**
     * Хэш мапа задач
     */
    private static final HashMap<Integer, Task> tasks = new HashMap<>();
    /**
     * Хэш мапа эпиков
     */
    private static final HashMap<Integer, Epic> epics = new HashMap<>();
    /**
     * Хэш мапа подзадач
     */
    private static final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    /**
     * Идентификатор задач
     */
    private static int id = 0;

    /**
     * Метод для генерации уникального идентификатора
     *
     * @return {@link #id}
     */
    public static int generateId() {
        id += 1;
        return id;
    }

    /**
     * Получение списка {@link #tasks}
     */
    public static ArrayList<Task> getTasks() {
        return new ArrayList<Task>(tasks.values());
    }

    /**
     * Получение списка {@link #epics}
     */
    public static ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epics.values()) ;
    }

    /**
     * Получение списка {@link #subtasks}
     */
    public static ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtasks.values());
    }

    /**
     * Удаление всех задач
     */
    public void deleteAllTasks() {
        tasks.clear();
    }

    /**
     * Удаление всех Эпиков
     */
    public void deleteAllEpics() {
        epics.clear();
    }

    /**
     * Удаление всех подзадач
     */
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    /**
     * Получение задачи по идентификатору
     */
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    /**
     * Получение эпика по идентификатору
     */
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    /**
     * Получение подзадачи по идентификатору
     */
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    /**
     * Создание задачи
     */
    public void createTask(String title, String description) {
        tasks.put(id, new Task(title, description, id, TaskStatus.NEW));
        id += 1;
    }

    /**
     * Создание эпика
     */
    public void createEpic(String title, String description) {
        epics.put(id, new Epic(title, description, id));
        id += 1;
    }

    /**
     * Создание подзадачи
     */
    public void createSubtask(String title, String description) {
        subtasks.put(id, new Subtask(title, description, id, TaskStatus.NEW));
        id += 1;
    }

    /**
     * Обновление задачи
     */
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * Обновление эпика
     */
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /**
     * Обновление подзадачи
     */
    public void updateSubtask(Subtask subtask) {
        tasks.put(subtask.getId(), subtask);
    }

    /**
     * Удаление задачи
     */
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    /**
     * Удаление эпика
     */
    public void deleteEpic(int id) {
        epics.remove(id);
    }

    /**
     * Удаление подзадачи
     */
    public void deleteSubtask(int id) {
        subtasks.remove(id);
    }

    /**
     * Получение списка подзадач определенного эпика
     */
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }
}
