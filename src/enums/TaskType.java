package enums;

public enum TaskType {
    TASK("TASK"),
    EPIC("EPIC"),
    SUBTASK("SUBTASK");

    private final String type;

    TaskType(final String status) {
        this.type = status;
    }

    public String getStatus() {
        return type;
    }

    @Override
    public String toString() {
        return "TaskType{"
                + "type='" + type + '\''
                + '}';
    }

    public static TaskType fromString(final String text) {
        for (TaskType taskType : TaskType.values()) {
            if (taskType.type.equalsIgnoreCase(text)) {
                return taskType;
            }
        }
        return null;
    }
}
