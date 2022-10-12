package enums;

public enum TaskStatus {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String status;

    TaskStatus(final String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "TaskStatus{"
                + "status='" + status + '\''
                + '}';
    }

    public static TaskStatus fromString(final String text) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.status.equalsIgnoreCase(text)) {
                return taskStatus;
            }
        }
        return null;
    }
}
