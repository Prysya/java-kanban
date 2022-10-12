package task;

import enums.TaskStatus;
import enums.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс с листом подзадач {@link Subtask}.
 */
public class Epic extends Task {
    /**
     * Количество параметров в csv строке.
     */
    private static final int CSV_PARAMS_COUNT = 6;
    /**
     * Тип таска.
     */
    private static final TaskType TASK_TYPE = TaskType.EPIC;
    /**
     * Мапа с подзадачами {@link Subtask}.
     */
    private final List<Integer> subtaskIds = new ArrayList<>();

    /**
     * Конструктор класса {@link Epic}.
     * @param title заголовок эпика
     * @param description описание эпика
     * */
    public Epic(final String title, final String description) {
        super(title, description, TaskStatus.NEW);
    }

    /**
     * @param subtasks мапа где ключ уникальный идентификатор,
     *                 а значение {@link Subtask}.
     * @return лист {@link Subtask}
     */
    public List<Subtask> getSubtasks(final Map<Integer, Subtask> subtasks) {
        return subtaskIds.stream().map(subtasks::get).collect(Collectors.toList());
    }

    /**
     * Добавление подзадачи в лист подзадач.
     *
     * @param id уникальный идентификатор подадачи({@link Subtask#id})
     */
    public void addSubtaskId(final int id) {
        subtaskIds.add(id);
    }

    /**
     * Удаление идентификатора подзадачи из {@link #subtaskIds}.
     *
     * @param id уникальный идентификатор подадачи({@link Subtask#id})
     */
    public void deleteSubtaskId(final int id) {
        subtaskIds.remove(id);
    }

    /**
     * Удаление всех идентификаторов подзадач подзадачи из {@link #subtaskIds}.
     */
    public void deleteAllSubtaskIds() {
        subtaskIds.clear();
    }

    /**
     * @param subtasksList список {@link Subtask}
     *                     Обновление статуса по состоянию подзадач.
     */
    public void updateStatus(final List<Subtask> subtasksList) {
        int newCount = (int) subtasksList.stream()
                .filter(subtask ->
                        subtask != null && subtask.getTaskStatus()
                                .equals(TaskStatus.NEW)).count();
        int doneCount = (int) subtasksList.stream()
                .filter(subtask -> subtask != null && subtask.getTaskStatus()
                        .equals(TaskStatus.DONE)).count();

        if (newCount == subtaskIds.size()) {
            setTaskStatus(TaskStatus.NEW);
        } else if (doneCount == subtaskIds.size()) {
            setTaskStatus(TaskStatus.DONE);
        } else {
            setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    /**
     * Если метод вызывается без параметров то сбрасывает статус задач.
     */
    public void updateStatus() {
        if (subtaskIds.isEmpty()) {
            setTaskStatus(TaskStatus.NEW);
        }
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

    /**
     * Получение {@link Task} из строки.
     * @param value строка в csv формате
     * @return {@link Task}
     */
    public static Epic fromString(final String value) {
        String[] values = value.split(",");

        final int idIndex = 0;
        final int titleIndex = 2;
        final int descriptionIndex = 4;

        // -1 потому что у эпика нет данных в колоке epic
        if (values.length < CSV_PARAMS_COUNT - 1) {
            return null;
        }

        Epic epic = new Epic(
                values[titleIndex],
                values[descriptionIndex]
        );
        epic.setId(Integer.parseInt(values[idIndex]));

        return epic;
    }
}
