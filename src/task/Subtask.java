package task;

import enums.TaskStatus;

/**
 * Класс для подзадач класса {@link Epic}
 */
public class Subtask extends Task {
    /**
     * Идентификатор эпика родителя
     */
    private final Integer parentEpicId;

    public Subtask(String title, String description, TaskStatus taskStatus, int parentEpicId) {
        super(title, description, taskStatus);
        this.parentEpicId = parentEpicId;
    }

    /**
     * @return {@link #parentEpicId}
     */
    public Integer getParentEpicId() {
        return parentEpicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", taskStatus=" + super.getTaskStatus() +
                ", parentEpicId=" + (parentEpicId == null ? "null" : parentEpicId) +
                '}';
    }
}
