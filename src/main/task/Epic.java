package main.task;

import main.enums.TaskStatus;
import main.enums.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс с листом подзадач {@link Subtask}.
 */
public class Epic extends Task {
    /**
     * Тип таска.
     */
    private static final TaskType TASK_TYPE = TaskType.EPIC;
    /**
     * Поле со списком уникальных идентификаторов подзадач.
     */
    private List<Integer> subtaskIds = new ArrayList<>();

    private LocalDateTime endTime;

    /**
     * Конструктор класса {@link Epic}.
     *
     * @param title       заголовок эпика
     * @param description описание эпика
     */
    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW, 0, null);
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

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
