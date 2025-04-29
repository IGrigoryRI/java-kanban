package ManagersPackage;

import TasksPackage.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, TaskInterface> tasks = new HashMap<>();
    private final Map<Integer, EpicInterface> epics = new HashMap<>();
    private final Map<Integer, SubTaskInterface> subTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 0;

    @Override
    public void putNewTask(Task newTask) {
        newTask.setID(id);
        tasks.put(id, newTask);
        createID();
    }

    @Override
    public void putNewEpic(Epic newEpic) {
        newEpic.setID(id);
        epics.put(id, newEpic);
        setEpicStatus(id);
        createID();
    }

    @Override
    public void putNewSubTask(SubTask newSubTask) {
        newSubTask.setID(id);
        subTasks.put(id, newSubTask);
        int epicId = subTasks.get(id).getEpicID();
        epics.get(epicId).getSubTasksID().add(id);
        setEpicStatus(epicId);
        createID();
    }

    @Override
    public List<TaskInterface> getAllTaskList() {
        for (TaskInterface task: tasks.values()) {
            historyManager.addHistory(task);
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTaskInterface> getAllSubTasksList() {
        for (SubTaskInterface subTask: subTasks.values()) {
            historyManager.addHistory(subTask);
        }
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<EpicInterface> getAllEpicsList() {
        for (EpicInterface epic: epics.values()) {
            historyManager.addHistory(epic);
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTaskInterface> getEpicAllSubTasks(int epicID) {
        List<SubTaskInterface> epicsSubTasks = new ArrayList<>();
        for (Integer subTaskID : epics.get(epicID).getSubTasksID()) {
            epicsSubTasks.add(subTasks.get(subTaskID));
        }
        return epicsSubTasks;
    }

    @Override
    public TaskInterface getTask(int taskID) {
        historyManager.addHistory(tasks.get(taskID));
        return tasks.get(taskID);
    }

    @Override
    public EpicInterface getEpic(int epicID) {
        historyManager.addHistory(epics.get(epicID));
        return epics.get(epicID);
    }

    @Override
    public SubTaskInterface getSubTask(int subTaskID) {
        historyManager.addHistory(subTasks.get(subTaskID));
        return subTasks.get(subTaskID);
    }

    @Override
    public void deleteTask(Integer taskID) {
        tasks.remove(taskID);
    }

    @Override
    public void deleteEpic(Integer epicID) {
        for (Integer subTaskID : epics.get(epicID).getSubTasksID()) {
            subTasks.remove(subTaskID);
        }
        epics.remove(epicID);
    }

    @Override
    public void deleteSubTask(Integer subTaskID) {
        int epicID = subTasks.get(subTaskID).getEpicID();
        epics.get(epicID).deleteSubTaskID(subTaskID);
        subTasks.remove(subTaskID);
        setEpicStatus(epicID);
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        for (Integer epicID : epics.keySet()) {
            if (!epics.get(epicID).getSubTasksID().isEmpty()) {
                epics.get(epicID).getSubTasksID().clear();
                epics.get(epicID).setStatus(Status.NEW);
            }
        }
    }

    @Override
    public void updateTask(TaskInterface task) {
        tasks.replace(task.getID(), task);
    }

    @Override
    public void updateSubTask(SubTaskInterface subTask) {
        subTasks.replace(subTask.getID(), subTask);
        setEpicStatus(subTask.getEpicID());
    }

    @Override
    public void updateEpic(EpicInterface epic) {
        epics.replace(epic.getID(), epic);
    }

    public List getHistory() {
        return historyManager.getHistory();
    }

    private void createID() {
        id++;
    }

    private void setEpicStatus(int epicID) {
        int newCounter = 0;
        int inProgressCounter = 0;
        int doneCounter = 0;

        for (int subTaskId : epics.get(epicID).getSubTasksID()) {
            if (subTasks.get(subTaskId).getStatus().equals(Status.NEW)) {
                newCounter++;
            }
            if (subTasks.get(subTaskId).getStatus().equals(Status.IN_PROGRESS)) {
                inProgressCounter++;
            }
            if (subTasks.get(subTaskId).getStatus().equals(Status.DONE)) {
                doneCounter++;
            }
        }

        if ((inProgressCounter + doneCounter) == 0) {
            epics.get(epicID).setStatus(Status.NEW);
        } else if ((newCounter + inProgressCounter == 0) && (doneCounter > 0)) {
            epics.get(epicID).setStatus(Status.DONE);
        } else {
            epics.get(epicID).setStatus(Status.IN_PROGRESS);
        }
    }
}