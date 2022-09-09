package task;

import enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс с листом подзадач {@link Subtask}
 */
public class Epic extends Task {
    /**
     * Мапа с подзадачами {@link Subtask}
     */
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    /**
     * @return лист {@link Subtask}
     */
    public List<Subtask> getSubtasks(Map<Integer, Subtask> subtasks) {
        return subtaskIds.stream().map(subtasks::get).collect(Collectors.toList());
    }

    /**
     * Добавление подзадачи в лист подзадач
     *
     * @param id уникальный идентификатор подадачи({@link Subtask#id})
     */
    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    /**
     * Удаление идентификатора подзадачи из {@link #subtaskIds}
     *
     * @param id уникальный идентификатор подадачи({@link Subtask#id})
     */
    public void deleteSubtaskId(Integer id) {
            subtaskIds.remove(id);
    }

    /**
     * Удаление всех идентификаторов подзадач подзадачи из {@link #subtaskIds}
     */
    public void deleteAllSubtaskIds() {
        subtaskIds.clear();
    }

    /**
     * @param subtasksList список {@link Subtask}
     *                     Обновление статуса по состоянию подзадач
     */
    public void updateStatus(List<Subtask> subtasksList) {
        int newCount = (int) subtasksList.stream()
                .filter(subtask -> subtask != null && subtask.getTaskStatus().equals(TaskStatus.NEW)).count();
        int doneCount = (int) subtasksList.stream()
                .filter(subtask -> subtask != null && subtask.getTaskStatus().equals(TaskStatus.DONE)).count();

        if (newCount == subtaskIds.size()) {
            setTaskStatus(TaskStatus.NEW);
        } else if (doneCount == subtaskIds.size()) {
            setTaskStatus(TaskStatus.DONE);
        } else {
            setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    /**
     * Если метод вызывается без параметров то сбрасывает статус задач
     */
    public void updateStatus() {
        if (subtaskIds.isEmpty()) {
            setTaskStatus(TaskStatus.NEW);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", taskStatus=" + super.getTaskStatus() +
                ", subtasks=" + subtaskIds +
                '}';
    }
}
