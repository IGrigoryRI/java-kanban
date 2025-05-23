package ManagersPackage;

import TasksPackage.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void putNewTask(Task newTask);

    void putNewEpic(Epic newEpic);

    void putNewSubTask(SubTask newSubTask);

    List<Task> getAllTaskList();

    List<SubTask> getAllSubTasksList();
    List<Epic> getAllEpicsList();

    List<SubTask> getEpicAllSubTasks(int epicID);

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