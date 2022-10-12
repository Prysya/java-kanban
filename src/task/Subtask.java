package task;

import enums.TaskStatus;
import enums.TaskType;

/**
 * Класс для подзадач класса {@link Epic}.
 */
public class Subtask extends Task {
    /**
     * Количество параметров в csv строке.
     */
    private static final int CSV_PARAMS_COUNT = 6;
    /**
     * Тип таска.
     */
    private static final TaskType TASK_TYPE = TaskType.SUBTASK;
    /**
     * Идентификатор эпика родителя.
     */
    private final Integer parentEpicId;

    /**
     * Конструктор класса {@link Subtask}.
     * @param title заголовок подтаска
     * @param description описание подтаска
     * @param taskStatus статус подтаска
     * @param parentEpicId уникальный идентификатор {@link Epic}
     * */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getId() + ","
                + TASK_TYPE.getStatus() + ","
                + getTitle() + ","
                + getTaskStatus().getStatus() + ","
                + getDescription() + ","
                + getParentEpicId();
    }

    /**
     * Получение {@link Subtask} из строки.
     * @param value строка в csv формате
     * @return {@link Subtask}
     */
    public static Subtask fromString(final String value) {
        String[] values = value.split(",");

        final int idIndex = 0;
        final int titleIndex = 2;
        final int subtaskStatusIndex = 3;
        final int descriptionIndex = 4;
        final int parentEpicIdIndex = 5;

        if (values.length < CSV_PARAMS_COUNT) {
            return null;
        }

        Subtask subtask = new Subtask(
                values[titleIndex],
                values[descriptionIndex],
                TaskStatus.fromString(values[subtaskStatusIndex]),
                Integer.parseInt(values[parentEpicIdIndex])
        );
        subtask.setId(Integer.parseInt(values[idIndex]));

        return subtask;
    }
}
