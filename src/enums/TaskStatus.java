package enums;

public enum TaskStatus {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "TaskStatus{" +
                "status='" + status + '\'' +
                '}';
    }
}
