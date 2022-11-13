package main.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.exceptions.BadRequestException;
import main.exceptions.MethodNotAllowedException;
import main.exceptions.NotFoundException;
import main.manager.HTTPTaskManager;
import main.serverHandlers.*;
import main.utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = GsonBuilders.getGsonWithLocalDateTime();
    private final HTTPTaskManager taskManager;
    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create();

        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/", new HttpTasksHandler());
        taskManager = Managers.getDefault();
    }

    /**
     * Метод для запуска сервера
     */
    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    /**
     * Метод для остановки сервера
     */
    public void stop() {
        server.stop(0);
    }

    /**
     * Класс хендлер контекста
     */
    class HttpTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String method = httpExchange.getRequestMethod();
                String path = httpExchange.getRequestURI().getRawPath();
                Map<String, String> queryMap = UrlHelpers.queryToMap(httpExchange.getRequestURI().getRawQuery());
                Integer id = queryMap.containsKey("id") ? UrlHelpers.parseQueryValueToInt(queryMap.get("id")) : null;

                TaskHandler taskHandler = new TaskHandler(id, gson, taskManager);
                EpicHandler epicHandler = new EpicHandler(id, gson, taskManager);
                SubtaskHandler subtaskHandler = new SubtaskHandler(id, gson, taskManager);
                SubtaskEpicHandler subtaskEpicHandler = new SubtaskEpicHandler(id, gson, taskManager);
                TasksHandler tasksHandler = new TasksHandler(gson, taskManager);
                HistoryHandler historyHandler = new HistoryHandler(gson, taskManager);

                // Проверка на вхождение в /tasks/
                if (Pattern.matches(RegExpVariables.TASKS_WITH_ADDITIONAL_URL, path)) {
                    switch (method) {
                        case "GET":
                            String json;

                            if (Pattern.matches(RegExpVariables.TASK_ENTRY, path)) {
                                json = taskHandler.getHandler();
                            } else if (Pattern.matches(RegExpVariables.EPIC_ENTRY, path)) {
                                json = epicHandler.getHandler();
                            } else if (Pattern.matches(RegExpVariables.SUBTASK_ENTRY, path)) {
                                json = subtaskHandler.getHandler();
                            } else if (Pattern.matches(RegExpVariables.EPIC_SUBTASKS_ENRTY, path)) {
                                json = subtaskEpicHandler.getHandler();
                            } else if (Pattern.matches(RegExpVariables.HISTORY_ENTRY, path)) {
                                json = historyHandler.getHandler();
                            } else {
                                // Ответ на запрос /tasks/
                                json = tasksHandler.getHandler();
                            }
                            ServerUtils.sendResponse(httpExchange, json, HttpURLConnection.HTTP_OK);
                            return;

                        case "POST":
                            InputStream inputStream = httpExchange.getRequestBody();
                            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

                            if (Pattern.matches(RegExpVariables.TASK_ENTRY, path)) {
                                taskHandler.postHandler(body);
                            } else if (Pattern.matches(RegExpVariables.EPIC_ENTRY, path)) {
                                epicHandler.postHandler(body);
                            } else if (Pattern.matches(RegExpVariables.SUBTASK_ENTRY, path)) {
                                subtaskHandler.postHandler(body);
                            } else {
                                throw new NotFoundException();
                            }
                            ServerUtils.sendResponse(httpExchange, "OK", HttpURLConnection.HTTP_CREATED);
                            return;

                        case "DELETE":
                            if (Pattern.matches(RegExpVariables.TASK_ENTRY, path)) {
                                taskHandler.deleteHandler();
                            } else if (Pattern.matches(RegExpVariables.EPIC_ENTRY, path)) {
                                epicHandler.deleteHandler();
                            } else if (Pattern.matches(RegExpVariables.SUBTASK_ENTRY, path)) {
                                subtaskHandler.deleteHandler();
                            } else {
                                throw new NotFoundException();
                            }
                            ServerUtils.sendResponse(httpExchange, "OK", HttpURLConnection.HTTP_OK);
                            return;
                        default:
                            throw new NotFoundException();
                    }
                } else {
                    throw new NotFoundException();
                }
            }
            catch (NotFoundException | BadRequestException | MethodNotAllowedException e) {
                httpExchange.sendResponseHeaders(e.getErrorCode(), 0);
            }
            finally {
                httpExchange.close();
            }
        }
    }
}
