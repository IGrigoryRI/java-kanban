package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public FileBackedTaskManager() {
        createFile();
        load();
    }

    private static final String HEAD = "id,type,name,status,description,epic";

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

    public void save() {
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

    public void load() {
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

    public static String taskToString(Task task) throws ManagerSaveException {
        switch (task.getType()) {
            case TASK -> {
                return String.format("%d,%s,%s,%s,%s,",
                        task.getId(),
                        task.getType(),
                        task.getName(),
                        task.getStatus(),
                        task.getDescription());
            }

            case EPIC -> {
                Epic epic = (Epic) task;

                return String.format("%d,%s,%s,%s,%s",
                        epic.getId(),
                        epic.getType(),
                        epic.getName(),
                        epic.getStatus(),
                        epic.getDescription());
            }

            case SUBTASK -> {
                SubTask subTask = (SubTask) task;

                return String.format("%d,%s,%s,%s,%s,%d,",
                        subTask.getId(),
                        subTask.getType(),
                        subTask.getName(),
                        subTask.getStatus(),
                        subTask.getDescription(),
                        subTask.getEpicId());
            }
            default -> throw new ManagerSaveException("Неизвестный тип задачи");
        }
    }

    public static Task taskFromString(String line) {
        String[] stringToTask = line.split(",");
        TaskType taskType = TaskType.valueOf(stringToTask[1]);

        try {
            switch (taskType) {
                case TASK -> {
                    Task task = new Task(stringToTask[2], stringToTask[4]);

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
                    SubTask subTask = new SubTask(
                            stringToTask[2],
                            stringToTask[4],
                            Integer.valueOf(stringToTask[5].trim()));

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
                Files.writeString(path, "id,type,name,status,description,epic\n");
            } catch (IOException e) {
                System.err.println("Не удалось создать файл tasks.csv: " + e.getMessage());
            }
        }
    }
}