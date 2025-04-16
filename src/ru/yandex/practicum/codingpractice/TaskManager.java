package ru.yandex.practicum.codingpractice;

import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int identifier = 0;

    public void generateNewTask(String name, String description) {
        Task task = new Task(name, description);
        tasks.put(identifier, task);
        identifier++;
    }

    public void generateNewEpic(String name, String description) {
        Epic epic = new Epic(name, description);
        epics.put(identifier, epic);
        setEpicStatus(identifier);
        identifier++;
    }

    public void generateNewSubTask(String name, String description, int epicID) {
        SubTask subTask = new SubTask(name, description, epicID);
        subTasks.put(identifier, subTask);
        setEpicStatus(epicID);
        pushSubTaskIDtoEpic(epicID, identifier);
        identifier++;
    }

    public void pushSubTaskIDtoEpic(int epicID, int subTaskID) {
        epics.get(epicID).getSubTasksID().add(subTaskID);
    }

    public void printAllTasksList() {
        for (Task tasks : tasks.values()) {
            System.out.println(tasks);
        }
    }

    public void printAllEpicsList() {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    public void printAllSubTasksList() {
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }

    public void printEpicAllSubTasks(int epicID) {
        if (epics.containsKey(epicID)) {
            for (SubTask subTask : subTasks.values()) {
                if (subTask.getEpicID() == epicID) {
                    System.out.println(subTask);
                }
            }
        }
    }

    public Task getTaskByID(int taskID) {
        Task task = tasks.get(taskID);
        return task;
    }

    public Epic getEpicByID(int EpicID) {
        Epic epic = epics.get(EpicID);
        return epic;
    }

    public SubTask getSubTaskByID(int subTaskID) {
        SubTask subTask = subTasks.get(subTaskID);
        return subTask;
    }

    public void deleteTaskByID(int taskID) {
        tasks.remove(taskID);
    }

    public void deleteEpicByID(int EpicID) {
        epics.remove(EpicID);
    }

    public void deleteSubTaskByID(int subTaskID) {
        int epicID = subTasks.get(subTaskID).getEpicID();
        deleteSubTaskIDfromEpic(epicID, subTaskID);
        subTasks.remove(subTaskID);
        setEpicStatus(epicID);
    }

    public void deleteSubTaskIDfromEpic(int epicID, int subTaskID) { // метод говна, переписать
        epics.get(epicID).deleteSubTaskID(subTaskID);
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void clearSubTasks() {
        subTasks.clear();
    }

    public void clearTracker() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        identifier = 0;
    }

    public void updateTaskStatus(Integer taskID, Status newStatus) {
        for (Integer ID : tasks.keySet()) {
            if (taskID.equals(ID)) {
                tasks.get(ID).setStatus(newStatus);
            }
        }
    }

    public void updateSubTaskStatus(Integer subTaskID, Status newStatus) {
        for (Integer ID : subTasks.keySet()) {
            if (subTaskID.equals(ID)) {
                subTasks.get(subTaskID).setStatus(newStatus);
                setEpicStatus(subTasks.get(subTaskID).getEpicID());
            }
        }
    }

    public void setEpicStatus(int epicID) {
        Status status;
        int newCounter = 0;
        int inProgressCounter = 0;
        int doneCounter = 0;

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicID() == epicID) {
                if (subTask.getStatus().equals(Status.NEW)) {
                    newCounter++;
                }
                if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                    inProgressCounter++;
                }
                if (subTask.getStatus().equals(Status.DONE)) {
                    doneCounter++;
                }
            }
        }

        if ((inProgressCounter + doneCounter) == 0) {
            status = Status.NEW;
        } else if ((newCounter + inProgressCounter == 0) && (doneCounter > 0)) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }

        epics.get(epicID).setStatus(status);
    }
}

