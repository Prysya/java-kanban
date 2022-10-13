package manager;

import enums.TaskType;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    /**
     * Кодировка файла.
     */
    private static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

    /**
     * Метод для загрузки истории из файла.
     *
     * @param file файл с историей
     */
    public static FileBackedTasksManager loadFromFile(final File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

        try {
            String content = Files.readString(Paths.get(file.getPath()));
            String[] lines = content.split(System.lineSeparator());

            // Если в файле только заголовок то файл пустой
            if (lines.length != 1) {
                String[] tasks = Arrays.copyOfRange(lines, 1, lines.length - 2);
                List<Integer> historyIdsList = historyFromString(lines[lines.length - 1]);
                Map<Integer, TaskType> typeByIndex = new HashMap<>();

                for (String taskLine : tasks) {
                    String[] lineAsArray = taskLine.split(",");

                    switch (Objects.requireNonNull(TaskType.fromString(lineAsArray[1]))) {
                        case TASK:
                            Task task = Task.fromString(taskLine);
                            if (task != null) {
                                fileBackedTasksManager.createTask(task);
                                typeByIndex.put(task.getId(), TaskType.TASK);
                            }
                            break;
                        case EPIC:
                            Epic epic = Epic.fromString(taskLine);
                            if (epic != null) {
                                fileBackedTasksManager.createEpic(epic);
                                typeByIndex.put(epic.getId(), TaskType.EPIC);
                            }
                            break;
                        case SUBTASK:
                            Subtask subtask = Subtask.fromString(taskLine);
                            if (subtask != null) {
                                fileBackedTasksManager.createSubtask(subtask);
                                typeByIndex.put(subtask.getId(), TaskType.SUBTASK);
                            }
                            break;
                        default:
                    }

                }


                historyIdsList.forEach(taskId -> {
                    TaskType type = typeByIndex.get(taskId);

                    if (type != null) {
                        switch (type) {
                            case TASK:
                                fileBackedTasksManager.getTaskById(taskId);
                                break;
                            case EPIC:
                                fileBackedTasksManager.getEpicById(taskId);
                                break;
                            case SUBTASK:
                                fileBackedTasksManager.getSubtaskById(taskId);
                                break;
                            default:
                        }
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return fileBackedTasksManager;
    }

    /**
     * Принимает менеджер истрии и парсит в строку.
     *
     * @param manager {@link HistoryManager}
     * @return возвращает отпаршеную строку
     */
    private static String historyToString(final HistoryManager manager) {
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
    private static List<Integer> historyFromString(final String value) {
        List<Integer> indexes = new ArrayList<>();

        String[] indexesFromString = value.split(",");

        if (indexesFromString.length > 0) {
            Arrays.stream(indexesFromString)
                    .forEach(index -> indexes.add(Integer.parseInt(index)));
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
        String[] values = value.split(",");

        final int idIndex = 0;
        final int titleIndex = 2;
        final int descriptionIndex = 4;

        // -1 потому что у эпика нет данных в колоке epic
        if (values.length < CSV_PARAMS_COUNT - 1) {
            return null;
        }

        Epic epic = new Epic(values[titleIndex], values[descriptionIndex]);
        epic.setId(Integer.parseInt(values[idIndex]));

        return epic;
    }

    /**
     * Получение {@link Subtask} из строки.
     *
     * @param value строка в csv формате
     * @return {@link Subtask}
     */
    public static Subtask subtaskFromString(String value) {
        String[] values = value.split(",");

        final int idIndex = 0;
        final int titleIndex = 2;
        final int subtaskStatusIndex = 3;
        final int descriptionIndex = 4;
        final int parentEpicIdIndex = 5;

        if (values.length < CSV_PARAMS_COUNT) {
            return null;
        }

        Subtask subtask = new Subtask(values[titleIndex], values[descriptionIndex], TaskStatus.fromString(values[subtaskStatusIndex]), Integer.parseInt(values[parentEpicIdIndex]));
        subtask.setId(Integer.parseInt(values[idIndex]));

        return subtask;
    }

    /**
     * Получение {@link Task} из строки.
     *
     * @param value строка в csv формате
     * @return {@link Task}
     */
    public static Task taskFromString(String value) {
        String[] values = value.split(",");

        final int idIndex = 0;
        final int titleIndex = 2;
        final int taskStatusIndex = 3;
        final int descriptionIndex = 4;

        // -1 потому что у таска нет данных в колоке epic
        if (values.length < CSV_PARAMS_COUNT - 1) {
            return null;
        }

        Task task = new Task(values[titleIndex], values[descriptionIndex], TaskStatus.fromString(values[taskStatusIndex]));
        task.setId(Integer.parseInt(values[idIndex]));

        return task;
    }

    /**
     * Метод для сохранения текущей истории в файл.
     */
    void save() {
        try (FileWriter fileWriter =
                     new FileWriter("history.csv", FILE_CHARSET)) {
            fileWriter.write("id,type,name,status,description,epic\n");


            Arrays.asList(getTasks(), getEpics(), getSubtasks())
                    .forEach(line -> line.forEach(task -> {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task getTaskById(final int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Epic getEpicById(final int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subtask getSubtaskById(final int subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(final int taskId) {
        super.deleteTask(taskId);
        save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEpic(final int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSubtask(final int subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }
}
