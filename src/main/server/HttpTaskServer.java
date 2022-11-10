package main.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.enums.Endpoints;
import main.manager.HTTPTaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.utils.GsonBuilders;
import main.utils.Managers;
import main.utils.UrlParsers;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = GsonBuilders.getGsonWithLocalDateTime();
    private final HTTPTaskManager taskManager;
    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create();

        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/", new TasksHandler());
        taskManager = Managers.getDefault();
    }

    private static void sendResponse(HttpExchange httpExchange, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(rCode, resp.length);
        httpExchange.getResponseBody().write(resp);
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
    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String method = httpExchange.getRequestMethod();
                String path = httpExchange.getRequestURI().getRawPath();
                Map<String, String> queryMap = UrlParsers.queryToMap(httpExchange.getRequestURI().getRawQuery());

                final String tasksRegexp = "^/" + Endpoints.TASKS.getEndpoint();

                // Проверка на вхождение в /tasks/
                if (Pattern.matches(tasksRegexp + "/[\\w/]*", path)) {
                    Integer id = queryMap.containsKey("id") ? UrlParsers.parseQueryValueToInt(queryMap.get("id")) : null;

                    switch (method) {
                        case "GET": // Проверка на вхождение в /tasks/task
                            if (Pattern.matches(tasksRegexp + "/" + Endpoints.TASK.getEndpoint() + "/?$", path)) {
                                // Проверка на присутствие query id
                                if (!Objects.isNull(id)) {
                                    if (id == -1) {
                                        httpExchange.sendResponseHeaders(403, 0);
                                        return;
                                    } else {
                                        Task task = taskManager.getTaskById(id);

                                        if (Objects.isNull(task)) {
                                            httpExchange.sendResponseHeaders(404, 0);
                                            return;
                                        } else {
                                            sendResponse(httpExchange, gson.toJson(task), 200);
                                        }
                                    }
                                } else {
                                    sendResponse(httpExchange, gson.toJson(taskManager.getTasks()), 200);
                                }
                            }

                            // Проверка на вхождение в /tasks/subtask
                            else if (Pattern.matches(tasksRegexp + "/" + Endpoints.SUBTASK.getEndpoint() + "/?$", path)) {
                                // Проверка на вхождение в /tasks/subtask/epic
                                if (Pattern.matches(tasksRegexp + "/" + Endpoints.SUBTASK.getEndpoint() + "/" + Endpoints.EPIC.getEndpoint() + "/?$", path)) {
                                    if (Objects.isNull(id) || id == -1) {
                                        httpExchange.sendResponseHeaders(Objects.isNull(id) ? 404 : 403, 0);
                                        return;
                                    } else {
                                        Epic epic = taskManager.getEpicById(id);

                                        if (Objects.isNull(epic)) {
                                            httpExchange.sendResponseHeaders(404, 0);
                                            return;
                                        } else {
                                            sendResponse(httpExchange, gson.toJson(taskManager.getEpicSubtasks(epic)), 200);
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(id)) {
                                        if (id == -1) {
                                            httpExchange.sendResponseHeaders(403, 0);
                                            return;
                                        } else {
                                            Subtask subtask = taskManager.getSubtaskById(id);

                                            if (Objects.isNull(subtask)) {
                                                httpExchange.sendResponseHeaders(404, 0);
                                                return;
                                            } else {
                                                sendResponse(httpExchange, gson.toJson(subtask), 200);
                                            }
                                        }
                                    } else {
                                        sendResponse(httpExchange, gson.toJson(taskManager.getSubtasks()), 200);
                                    }
                                }
                            }

                            // Проверка на вхождение в /tasks/epic
                            else if (Pattern.matches(tasksRegexp + "/" + Endpoints.EPIC.getEndpoint() + "/?$", path)) {
                                // Проверка на присутствие query id
                                if (!Objects.isNull(id)) {
                                    if (id == -1) {
                                        httpExchange.sendResponseHeaders(403, 0);
                                        return;
                                    } else {
                                        Epic epic = taskManager.getEpicById(id);

                                        if (Objects.isNull(epic)) {
                                            httpExchange.sendResponseHeaders(404, 0);
                                            return;
                                        } else {
                                            sendResponse(httpExchange, gson.toJson(epic), 200);
                                        }
                                    }
                                } else {
                                    sendResponse(httpExchange, gson.toJson(taskManager.getEpics()), 200);
                                }
                            }

                            // Проверка на вхождение в /tasks/history
                            else if (Pattern.matches(tasksRegexp + "/" + Endpoints.HISTORY.getEndpoint() + "/?$", path)) {
                                sendResponse(httpExchange, gson.toJson(taskManager.getHistory()), 200);
                            }

                            // Ответ на запрос /tasks/
                            else {
                                sendResponse(httpExchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
                            }
                            break;

                        case "POST":
                            InputStream inputStream = httpExchange.getRequestBody();
                            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

                            if (Pattern.matches(tasksRegexp + "/" + Endpoints.TASK.getEndpoint() + "/?$", path)) {
                                try {
                                    Task task = gson.fromJson(body, Task.class);

                                    taskManager.createTask(task);

                                    sendResponse(httpExchange, "OK", 201);
                                } catch (JsonSyntaxException e) {
                                    httpExchange.sendResponseHeaders(401, 0);
                                    return;
                                }
                            }

                            // Проверка на вхождение в /tasks/subtask
                            else if (Pattern.matches(tasksRegexp + "/" + Endpoints.SUBTASK.getEndpoint() + "/?$", path)) {
                                try {
                                    Subtask subtask = gson.fromJson(body, Subtask.class);
                                    if (Objects.isNull(subtask)) {
                                        httpExchange.sendResponseHeaders(401, 0);
                                        return;
                                    }
                                    Epic epic = taskManager.getEpicById(subtask.getParentEpicId());

                                    if (Objects.isNull(epic)) {
                                        sendResponse(httpExchange, "Эпик с таким id не найден", 403);
                                        return;
                                    } else {
                                        taskManager.createSubtask(subtask);
                                        sendResponse(httpExchange, "OK", 201);
                                        System.out.println(epic.getSubtaskIds());
                                    }

                                } catch (JsonSyntaxException e) {
                                    httpExchange.sendResponseHeaders(401, 0);
                                    return;
                                } catch (NullPointerException e) {
                                    System.out.println(e);
                                }
                            }

                            // Проверка на вхождение в /tasks/epic
                            else if (Pattern.matches(tasksRegexp + "/" + Endpoints.EPIC.getEndpoint() + "/?$", path)) {
                                try {
                                    Epic epic = gson.fromJson(body, Epic.class);

                                    taskManager.createEpic(new Epic(epic.getTitle(), epic.getDescription()));
                                    sendResponse(httpExchange, "OK", 201);
                                } catch (JsonSyntaxException e) {
                                    httpExchange.sendResponseHeaders(401, 0);
                                    return;
                                }
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                                return;
                            }
                            break;

                        case "DELETE":
                            if (Pattern.matches(tasksRegexp + "/" + Endpoints.TASK.getEndpoint() + "/?$", path)) {
                                // Проверка на присутствие query id
                                if (!Objects.isNull(id)) {
                                    if (id == -1) {
                                        httpExchange.sendResponseHeaders(403, 0);
                                        return;
                                    } else {
                                        Task task = taskManager.getTaskById(id);

                                        if (Objects.isNull(task)) {
                                            httpExchange.sendResponseHeaders(404, 0);
                                            return;
                                        } else {
                                            taskManager.deleteTask(id);
                                            sendResponse(httpExchange, "OK", 200);

                                        }
                                    }
                                } else {
                                    taskManager.deleteAllTasks();
                                    sendResponse(httpExchange, "OK", 200);

                                }
                            }

                            // Проверка на вхождение в /tasks/subtask
                            else if (Pattern.matches(tasksRegexp + "/" + Endpoints.SUBTASK.getEndpoint() + "/?$", path)) {
                                // Проверка на вхождение в /tasks/subtask/epic
                                if (!Objects.isNull(id)) {
                                    if (id == -1) {
                                        httpExchange.sendResponseHeaders(403, 0);
                                        return;
                                    } else {
                                        Subtask subtask = taskManager.getSubtaskById(id);

                                        if (Objects.isNull(subtask)) {
                                            httpExchange.sendResponseHeaders(404, 0);
                                            return;
                                        } else {
                                            taskManager.deleteSubtask(id);
                                            sendResponse(httpExchange, "OK", 200);

                                        }
                                    }
                                } else {
                                    taskManager.deleteAllSubtasks();
                                    sendResponse(httpExchange, "OK", 200);

                                }
                            }

                            // Проверка на вхождение в /tasks/epic
                            else if (Pattern.matches(tasksRegexp + "/" + Endpoints.EPIC.getEndpoint() + "/?$", path)) {
                                // Проверка на присутствие query id
                                if (!Objects.isNull(id)) {
                                    if (id == -1) {
                                        httpExchange.sendResponseHeaders(403, 0);
                                        return;
                                    } else {
                                        Epic epic = taskManager.getEpicById(id);

                                        if (Objects.isNull(epic)) {
                                            httpExchange.sendResponseHeaders(404, 0);
                                            return;
                                        } else {
                                            taskManager.deleteEpic(id);
                                            sendResponse(httpExchange, "OK", 200);

                                        }
                                    }
                                } else {
                                    taskManager.deleteAllEpics();
                                    sendResponse(httpExchange, "OK", 200);
                                }
                            }
                            break;

                        default:
                    }
                }
                // Проверка на /tasks без дополнительных параметров
                else if (Pattern.matches(tasksRegexp + "/?$", path)) {
                    if ("GET".equals(method)) { // Проверка на вхождение в /tasks/task
                        sendResponse(httpExchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                } else {
                    System.out.println(method);
                    System.out.println(path);
                    httpExchange.sendResponseHeaders(404, 0);
                }
            } finally {
                httpExchange.close();
            }
        }
    }
}
