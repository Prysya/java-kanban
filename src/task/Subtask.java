package task;

import enums.TaskStatus;

/**
 * Класс для подзадач класса {@link Epic}
 */
public class Subtask extends Task {
    private Integer parentEpicId;

    public Subtask(String title, String description, int id, TaskStatus taskStatus) {
        super(title, description, id, taskStatus);
    }

    /**
     * @param parentEpicId идентификатор эпика родителя
     */
    public void setParentEpicId(Integer parentEpicId) {
        this.parentEpicId = parentEpicId;
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
