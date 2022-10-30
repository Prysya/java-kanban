package main.manager;

import main.enums.TaskFromString;
import main.enums.TaskStatus;
import main.enums.TaskType;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    /**
     * Количество параметров в csv строке.
     */
    private static final int CSV_PARAMS_COUNT = 8;
    /**
     * Кодировка файла.
     */
    private static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

    /**
     * Метод для загрузки истории из файла.
     *
     * @param file файл с историей
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

        if (Objects.isNull(file)) {
            return fileBackedTasksManager;
        }

        List<String> lines = getDataFromFile(file);

        final int HEADER_SIZE = 1;

        if (Objects.isNull(lines) || lines.size() < HEADER_SIZE || HEADER_SIZE > lines.size() - 2) {
            return fileBackedTasksManager;
        }

        // Обрезка листа от заголовка до футера где предпоследняя стркоа отступ, а последняя список идентификаторов
        Map<Integer, TaskType> typeByIndex = parseAndfillTasksIntoManager(fileBackedTasksManager, lines.subList(HEADER_SIZE, lines.size() - 2));

        parseAndFillHistoryIntoManager(fileBackedTasksManager, historyFromString(lines.get(lines.size() - 1)), typeByIndex);

        return fileBackedTasksManager;
    }

    /**
     * Метод для получения листа строк
     *
     * @param file файл с историей
     */
    private static List<String> getDataFromFile(File file) {
        try {
            String content = Files.readString(Paths.get(file.getPath()));
            return new ArrayList<>(Arrays.asList(content.split(System.lineSeparator())));
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
            return null;
        }
    }

    /**
     * Метод для заполнения тасок у менеджера
     *
     * @param manager менеджер для заполнения
     * @param tasks   лист задач в виде строки
     * @return мапу типов задач, где ключ, это уникальный идентификатор
     */
    private static Map<Integer, TaskType> parseAndfillTasksIntoManager(FileBackedTasksManager manager, List<String> tasks) {
        Map<Integer, TaskType> typeByIndex = new HashMap<>();

        for (String taskLine : tasks) {
            String[] lineAsArray = taskLine.split(",");

            TaskType type = TaskType.fromString(lineAsArray[1].trim());

            if (Objects.isNull(type)) continue;

            switch (type) {
                case TASK:
                    Task task = taskFromString(taskLine);
                    if (task != null) {
                        manager.createTask(task);
                        typeByIndex.put(task.getId(), TaskType.TASK);
                    }
                    break;
                case EPIC:
                    Epic epic = epicFromString(taskLine);
                    if (epic != null) {
                        manager.createEpic(epic);
                        typeByIndex.put(epic.getId(), TaskType.EPIC);
                    }
                    break;
                case SUBTASK:
                    Subtask subtask = subtaskFromString(taskLine);
                    if (subtask != null) {
                        manager.createSubtask(subtask);
                        typeByIndex.put(subtask.getId(), TaskType.SUBTASK);
                    }
                    break;
                default:
            }
        }

        return typeByIndex;
    }

    /**
     * Метод для заполнения истории у менеджера
     *
     * @param manager     менеджер для заполнения
     * @param historyIds  лист идентификаторов задач
     * @param typeByIndex мапа типов задач, где ключ, это уникальный идентификатор
     */
    private static void parseAndFillHistoryIntoManager(FileBackedTasksManager manager, List<Integer> historyIds, Map<Integer, TaskType> typeByIndex) {
        historyIds.forEach(taskId -> {
            TaskType type = typeByIndex.get(taskId);

            if (type != null) {
                switch (type) {
                    case TASK:
                        manager.getTaskById(taskId);
                        break;
                    case EPIC:
                        manager.getEpicById(taskId);
                        break;
                    case SUBTASK:
                        manager.getSubtaskById(taskId);
                        break;
                    default:
                }
            }
        });
    }

    /**
     * Принимает менеджер истрии и парсит в строку.
     *
     * @param manager {@link HistoryManager}
     * @return возвращает отпаршеную строку
     */
    private static String historyToString(HistoryManager manager) {
        List<String> indexes = new ArrayList<>();

        manager.getHistory().forEach(task -> indexes.add(Integer.toString(task.getId())));

        return String.join(",", indexes);
    }

    /**
     * Принимает строку и парсит в лист идентификаторов.
     *
     * @param value отпаршеная строка
     * @return лист идентификаторво задач
     */
    private static List<Integer> historyFromString(String value) {
        List<Integer> indexes = new ArrayList<>();

        String[] indexesFromString = value.split(",");

        if (indexesFromString.length > 0) {
            Arrays.stream(indexesFromString).forEach(index -> indexes.add(Integer.parseInt(index.trim())));
        }

        return indexes;
    }

    /**
     * Получение {@link Epic} из строки.
     *
     * @param value строка в csv формате
     * @return {@link Epic}
     */
    public static Epic epicFromString(String value) {
        List<String> values = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());

        // -1 потому что у эпика нет данных в колоке epic
        if (values.size() < CSV_PARAMS_COUNT - 1) {
            return null;
        }

        Epic epic = new Epic(
                values.get(TaskFromString.TITLE.getIndex()),
                values.get(TaskFromString.DESCRIPTION.getIndex())
        );
        epic.setId(Integer.parseInt(values.get(TaskFromString.ID.getIndex())));

        return epic;
    }

    /**
     * Получение {@link Subtask} из строки.
     *
     * @param value строка в csv формате
     * @return {@link Subtask}
     */
    public static Subtask subtaskFromString(String value) {
        List<String> values = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());

        if (values.size() < CSV_PARAMS_COUNT) {
            return null;
        }

        Subtask subtask = new Subtask(
                values.get(TaskFromString.TITLE.getIndex()),
                values.get(TaskFromString.DESCRIPTION.getIndex()),
                TaskStatus.fromString(values.get(TaskFromString.STATUS.getIndex())),
                Integer.parseInt(values.get(TaskFromString.DURATION.getIndex())),
                Objects.equals(values.get(TaskFromString.START_DATE.getIndex()), "null") ? null :LocalDateTime.parse(values.get(TaskFromString.START_DATE.getIndex())),
                Integer.parseInt(values.get(TaskFromString.PARENT_EPIC_ID.getIndex())));
        subtask.setId(Integer.parseInt(values.get(TaskFromString.ID.getIndex())));

        return subtask;
    }

    /**
     * Получение {@link Task} из строки.
     *
     * @param value строка в csv формате
     * @return {@link Task}
     */
    public static Task taskFromString(String value) {
        List<String> values = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());

        // -1 потому что у таски нет данных в колоке epic
        if (values.size() < CSV_PARAMS_COUNT - 1) {
            return null;
        }

        Task task = new Task(
                values.get(TaskFromString.TITLE.getIndex()),
                values.get(TaskFromString.DESCRIPTION.getIndex()),
                TaskStatus.fromString(values.get(TaskFromString.STATUS.getIndex())),
                Integer.parseInt(values.get(TaskFromString.DURATION.getIndex())),
                Objects.equals(values.get(TaskFromString.START_DATE.getIndex()), "null") ? null :LocalDateTime.parse(values.get(TaskFromString.START_DATE.getIndex()))
        );
        task.setId(Integer.parseInt(values.get(TaskFromString.ID.getIndex())));

        return task;
    }

    /**
     * Метод для сохранения текущей истории в файл.
     */
    void save() {
        try (FileWriter fileWriter = new FileWriter("history.csv", FILE_CHARSET)) {
            fileWriter.write("id,type,name,status,description,duration,startTime,epic\n");


            Arrays.asList(getTasks(), getEpics(), getSubtasks()).forEach(line -> line.forEach(task -> {
                try {
                    fileWriter.write(task + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));

            fileWriter.write("\n" + historyToString(getInMemoryHistoryManager()));
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }
}
