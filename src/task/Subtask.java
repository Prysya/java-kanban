package task;

import enums.TaskStatus;
import enums.TaskType;

/**
 * Класс для подзадач класса {@link Epic}.
 */
public class Subtask extends Task {
    /**
     * Тип таска.
     */
    private static final TaskType TASK_TYPE = TaskType.SUBTASK;
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
            final String title,
            final String description,
            final TaskStatus taskStatus,
            final int parentEpicId
    ) {
        super(title, description, taskStatus);
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
                + TASK_TYPE.getStatus() + ","
                + getTitle() + ","
                + getTaskStatus().getStatus() + ","
                + getDescription() + ","
                + getParentEpicId();
    }
}
