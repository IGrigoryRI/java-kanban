package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String HEAD = "id,type,name,status,description,startTime,duration,epic";

    public FileBackedTaskManager() {
        createFile();
        load();
    }

    @Override
    public void putNewTask(Task newTask) {
        super.putNewTask(newTask);
        save();
    }

    @Override
    public void putNewEpic(Epic newEpic) {
        super.putNewEpic(newEpic);
        save();
    }

    @Override
    public void putNewSubTask(SubTask newSubTask) {
        super.putNewSubTask(newSubTask);
        save();
    }

    @Override
    public void deleteTask(Integer taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(Integer epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubTask(Integer subTaskId) {
        super.deleteSubTask(subTaskId);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.csv"))) {
            writer.write(HEAD);
            writer.newLine();

            for (Task task : getAll()) {
                writer.write(taskToString(task));
                writer.newLine();
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при сохранении задач в файл", e);
        }
    }

    private void load() {
        try {
            String data = Files.readString(Path.of("tasks.csv"));
            String[] dataLines = data.split("\n");
            List<Task> allTasks = new ArrayList<>();

            for (int i = 1; i < dataLines.length; i++) {
                Task task = taskFromString(dataLines[i]);
                allTasks.add(task);
            }

            loadAll(allTasks);

        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при чтении файла", e);
        }
    }

    private static String taskToString(Task task) {
        return task.taskToString();
    }

    private static Task taskFromString(String line) {
        String[] stringToTask = line.trim().split(",");
        TaskType taskType = TaskType.valueOf(stringToTask[1]);

        try {
            switch (taskType) {
                case TASK -> {
                    LocalDateTime startTime = LocalDateTime.parse(stringToTask[5]);
                    Duration duration = Duration.ofMinutes(Integer.parseInt(stringToTask[6]));

                    Task task = new Task(stringToTask[2], stringToTask[4], duration, startTime);

                    task.setTaskType(taskType);
                    task.setId(Integer.valueOf(stringToTask[0]));
                    task.setStatus(Status.valueOf(stringToTask[3]));

                    return task;
                }

                case EPIC -> {
                    Epic epic = new Epic(stringToTask[2], stringToTask[4]);

                    epic.setTaskType(taskType);
                    epic.setId(Integer.valueOf(stringToTask[0]));
                    epic.setStatus(Status.valueOf(stringToTask[3]));

                    return epic;
                }

                case SUBTASK -> {
                    LocalDateTime startTime = LocalDateTime.parse(stringToTask[5]);
                    Duration duration = Duration.ofMinutes(Integer.parseInt(stringToTask[6]));

                    SubTask subTask = new SubTask(
                            stringToTask[2],
                            stringToTask[4],
                            duration,
                            startTime,
                            Integer.valueOf(stringToTask[7].trim()));

                    subTask.setTaskType(taskType);
                    subTask.setId(Integer.valueOf(stringToTask[0]));
                    subTask.setStatus(Status.valueOf(stringToTask[3]));

                    return subTask;
                }

                default -> throw new ManagerSaveException("Неизвестный тип задачи");
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException("Ошибка чтения задачи. Некорректные данные");
        }
    }

    private void createFile() {
        Path path = Path.of("tasks.csv");
        if (!Files.exists(path)) {
            try {
                Files.writeString(path, HEAD + "\n");
            } catch (IOException e) {
                System.err.println("Не удалось создать файл tasks.csv: " + e.getMessage());
            }
        }
    }
}