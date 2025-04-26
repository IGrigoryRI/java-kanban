package TaskApp;

import java.util.ArrayList;

public interface TaskManager {
    void putNewTask(Task newTask);

    void putNewEpic(Epic newEpic);

    void putNewSubTask(SubTask newSubTask);

    ArrayList<Task> getAllTaskList();

    ArrayList<SubTask> getAllSubTasksList();

    ArrayList<Epic> getAllEpicsList();

    ArrayList<SubTask> getEpicAllSubTasks(int epicID);

    Task getTask(int taskID);

    Epic getEpic(int epicID);

    SubTask getSubTask(int subTaskID);

    void deleteTask(Integer taskID);

    void deleteEpic(Integer epicID);

    void deleteSubTask(Integer subTaskID);

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);
}