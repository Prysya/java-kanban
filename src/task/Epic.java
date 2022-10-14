package task;

import enums.TaskStatus;
import enums.TaskType;

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
     * Список с подзадачами {@link Subtask}.
     */
    private List<Integer> subtaskIds = new ArrayList<>();

    /**
     * Конструктор класса {@link Epic}.
     *
     * @param title       заголовок эпика
     * @param description описание эпика
     */
    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    @Override
    public String toString() {
        return getId() + ","
                + TASK_TYPE.getStatus() + ","
                + getTitle() + ","
                + getTaskStatus().getStatus() + ","
                + getDescription() + ",";
    }

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
}
