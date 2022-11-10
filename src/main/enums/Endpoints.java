package main.enums;

public enum Endpoints {
    TASKS("tasks"),
    TASK("task"),
    EPIC("epic"),
    SUBTASK("subtask"),
    HISTORY("history");


    private final String endpoint;

    Endpoints(final String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
