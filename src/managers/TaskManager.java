package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    void putNewTask(Task newTask);

    void putNewEpic(Epic newEpic);

    void putNewSubTask(SubTask newSubTask);

    List<Task> getAllTaskList();

    List<SubTask> getAllSubTasksList();

    List<Epic> getAllEpicsList();

    List<SubTask> getEpicAllSubTasks(int epicId);

    TreeSet<Task> getPrioritizedTasks();

    List<? extends Task> getHistory();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubTask(int subTaskId);

    void deleteTask(Integer taskId);

    void deleteEpic(Integer epicId);

    void deleteSubTask(Integer subTaskId);

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);
}