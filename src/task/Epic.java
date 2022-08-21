package task;

import enums.TaskStatus;

import java.util.HashMap;

/**
 * Класс с листом подзадач {@link Subtask}
 */
public class Epic extends Task {
    /**
     * Мапа с подзадачами {@link Subtask}
     */
    final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String title, String description, int id) {
        super(title, description, id, TaskStatus.NEW);
    }

    /**
     * @return {@link #subtasks}
     */
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    /**
     * Добавление подзадачи в лист подзадач
     *
     * @param subtask {@link Subtask}
     */
    public void addSubtask(Subtask subtask) {
        subtask.setParentEpicId(getId());

        subtasks.put(subtask.getId(), subtask);

        updateStatus();
    }

    /**
     * Удаление подзадачи из листа подзадач
     *
     * @param id - уникальный идентификатор подзадачи
     */
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            subtask.setParentEpicId(null);

            subtasks.remove(id);

            updateStatus();
        }

    }

    /**
     * Обновление статуса по состоянию подзадач
     */
    void updateStatus() {
        int newCount = (int) subtasks.values().stream()
                .filter(subtask -> subtask.getTaskStatus().equals(TaskStatus.NEW)).count();
        int doneCount = (int) subtasks.values().stream()
                .filter(subtask -> subtask.getTaskStatus().equals(TaskStatus.DONE)).count();

        if (newCount == subtasks.size()) {
            setTaskStatus(TaskStatus.NEW);
        } else if (doneCount == subtasks.size()) {
            setTaskStatus(TaskStatus.DONE);
        } else {
            setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", taskStatus=" + super.getTaskStatus() +
                ", subtasks=" + subtasks +
                '}';
    }
}
