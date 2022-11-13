package main.task;

import main.enums.TaskStatus;
import main.enums.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс для подзадач класса {@link Epic}.
 */
public class Subtask extends Task {
    /**
     * Идентификатор эпика родителя.
     */
    private Integer parentEpicId;

    /**
     * Конструктор класса {@link Subtask}.
     *
     * @param title        заголовок подтаска
     * @param description  описание подтаска
     * @param taskStatus   статус подтаска
     * @param parentEpicId уникальный идентификатор {@link Epic}
     */
    public Subtask(
            String title,
            String description,
            TaskStatus taskStatus,
            int duration,
            LocalDateTime startTime,
            int parentEpicId
    ) {
        super(TaskType.SUBTASK, title, description, taskStatus, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    /**
     * @return {@link #parentEpicId}
     */
    public Integer getParentEpicId() {
        return parentEpicId;
    }

    public void setParentEpicId(Integer parentEpicId) {
        this.parentEpicId = parentEpicId;
    }

    @Override
    public String toString() {
        return getId() + ","
                + getTaskType().getStatus() + ","
                + getTitle() + ","
                + getTaskStatus().getStatus() + ","
                + getDescription() + ","
                + getDuration() + ","
                + getStartTime() + ","
                + getParentEpicId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subtask)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Subtask subtask = (Subtask) o;
        return Objects.equals(getParentEpicId(), subtask.getParentEpicId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getParentEpicId());
    }
}
