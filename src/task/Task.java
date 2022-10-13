package task;

import enums.TaskStatus;
import enums.TaskType;

/**
 * Основной класс задач.
 */
public class Task {
    /**
     * Количество параметров в csv строке.
     */
    private static final int CSV_PARAMS_COUNT = 6;
    /**
     * Тип таска.
     */
    private static final TaskType TASK_TYPE = TaskType.TASK;
    /**
     * Заголовок задачи.
     */
    protected final String title;
    /**
     * Описание задачи.
     */
    protected final String description;
    /**
     * Уникальный идентификатор задачи.
     */
    protected int id;
    /**
     * Текущий статус задачи.
     */
    protected TaskStatus taskStatus;

    /**
     * Конструктор класса {@link Task}.
     * @param title заголовок таска
     * @param description описание таска
     * @param taskStatus статус таска
     * */
    public Task(final String title, final String description, final TaskStatus taskStatus) {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    /**
     * @return {@link #title}
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return {@link #description}
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * Назначение нового {@link #id}.
     * @param id {@link #id}
     * */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return {@link #taskStatus}
     */
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    /**
     * @param taskStatus {@link #taskStatus}
     */
    protected void setTaskStatus(final TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getId() + ","
                + TASK_TYPE.getStatus() + ","
                + getTitle() + ","
                + getTaskStatus().getStatus() + ","
                + getDescription() + ",";
    }
}
