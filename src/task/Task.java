package task;

import enums.TaskStatus;

/**
 * Основной класс задач
 */
public class Task {
    /**
     * Заголовок задачи
     */
    protected final String title;
    /**
     * Описание задачи
     */
    protected final String description;
    /**
     * Уникальный идентификатор задачи
     */
    protected int id;
    /**
     * Текущий статус задачи
     */
    protected TaskStatus taskStatus;

    public Task(String title, String description, TaskStatus taskStatus) {
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

    public void setId(int id) {
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
    protected void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus.getStatus() +
                '}';
    }
}
