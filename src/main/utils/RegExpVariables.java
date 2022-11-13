package main.utils;

import main.enums.Endpoints;

public class RegExpVariables {
    public static String END_OF_URL = "/?$";
    public static String TASKS_ENTRY = "^/" + Endpoints.TASKS.getEndpoint();
    public static String TASKS_WITH_ADDITIONAL_URL = TASKS_ENTRY + "/[\\w/]*";
    public static String TASK_ENTRY = TASKS_ENTRY + "/" + Endpoints.TASK.getEndpoint() + END_OF_URL;
    public static String EPIC_ENTRY = TASKS_ENTRY + "/" + Endpoints.EPIC.getEndpoint() + END_OF_URL;
    public static String SUBTASK_ENTRY = TASKS_ENTRY + "/" + Endpoints.SUBTASK.getEndpoint() + END_OF_URL;
    public static String HISTORY_ENTRY = TASKS_ENTRY + "/" + Endpoints.HISTORY.getEndpoint() + END_OF_URL;
    public static String EPIC_SUBTASKS_ENRTY = TASKS_ENTRY +
            "/" +
            Endpoints.SUBTASK.getEndpoint() +
            "/" +
            Endpoints.EPIC.getEndpoint() +
            END_OF_URL;

    private RegExpVariables() {
    }
}
