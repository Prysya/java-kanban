package main.task;

import main.enums.TaskStatus;
import main.enums.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Основной класс задач.
 */
public class Task {
    /**
     * Тип таска.
     */
    private static final TaskType TASK_TYPE = TaskType.TASK;
    /**
     * Уникальный идентификатор задачи.
     */
    protected int id;
    /**
     * Заголовок задачи.
     */
    protected String title;
    /**
     * Описание задачи.
     */
    protected String description;
    /**
     * Текущий статус задачи.
     */
    protected TaskStatus taskStatus;
    /**
     * Продолжительность задачи, оценка того, сколько времени она займёт в минутах
     */
    protected int duration;
    /**
     * дата, когда предполагается приступить к выполнению задачи
     */
    protected LocalDateTime startTime;


    /**
     * Конструктор класса {@link Task}.
     *
     * @param title       заголовок таска
     * @param description описание таска
     * @param taskStatus  статус таска
     */
    public Task(String title, String description, TaskStatus taskStatus, int duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    /**
     * @return {@link #title}
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title {@link #title}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return {@link #description}
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description {@link #description}
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * Назначение нового {@link #id}.
     *
     * @param id {@link #id}
     */
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
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * @return {@link #duration}
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration {@link #duration}
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return {@link #startTime}
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime {@link #startTime}
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Время завершения задачи
     */
    public LocalDateTime getEndTime() {
        if (Objects.isNull(startTime)) {
            return null;
        }

        return startTime.plusMinutes(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return getId() == task.getId() && getDuration() == task.getDuration() && Objects.equals(getTitle(), task.getTitle()) && Objects.equals(getDescription(), task.getDescription()) && getTaskStatus() == task.getTaskStatus() && Objects.equals(getStartTime(), task.getStartTime());
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + taskStatus.hashCode();
        result = 31 * result + duration;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getId() + ","
                + TASK_TYPE.getStatus() + ","
                + getTitle() + ","
                + getTaskStatus().getStatus() + ","
                + getDescription() + ","
                + getDuration() + ","
                + getStartTime() + ",";
    }
}
