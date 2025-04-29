package ManagersPackage;

import TasksPackage.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void putNewTask(Task newTask);

    void putNewEpic(Epic newEpic);

    void putNewSubTask(SubTask newSubTask);

    List<TaskInterface> getAllTaskList();

    List<SubTaskInterface> getAllSubTasksList();
    List<EpicInterface> getAllEpicsList();

    List<SubTaskInterface> getEpicAllSubTasks(int epicID);

    TaskInterface getTask(int taskID);

    EpicInterface getEpic(int epicID);

    SubTaskInterface getSubTask(int subTaskID);

    void deleteTask(Integer taskID);

    void deleteEpic(Integer epicID);

    void deleteSubTask(Integer subTaskID);

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    void updateTask(TaskInterface task);

    void updateSubTask(SubTaskInterface subTask);

    void updateEpic(EpicInterface epic);
}