package managers;

import tasks.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
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
    public List<Task> getAllTaskList() {
        for (Task task: tasks.values()) {
            historyManager.add(task);
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasksList() {
        for (SubTask subTask: subTasks.values()) {
            historyManager.add(subTask);
        }
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpicsList() {
        for (Epic epic: epics.values()) {
            historyManager.add(epic);
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getEpicAllSubTasks(int epicID) {
        List<SubTask> epicsSubTasks = new ArrayList<>();
        for (Integer subTaskID : epics.get(epicID).getSubTasksID()) {
            epicsSubTasks.add(subTasks.get(subTaskID));
        }
        return epicsSubTasks;
    }

    @Override
    public Task getTask(int taskID) {
        historyManager.add(tasks.get(taskID));
        return tasks.get(taskID);
    }

    @Override
    public Epic getEpic(int epicID) {
        historyManager.add(epics.get(epicID));
        return epics.get(epicID);
    }

    @Override
    public SubTask getSubTask(int subTaskID) {
        historyManager.add(subTasks.get(subTaskID));
        return subTasks.get(subTaskID);
    }

    @Override
    public void deleteTask(Integer taskID) {
        tasks.remove(taskID);
        historyManager.remove(taskID);
    }

    @Override
    public void deleteEpic(Integer epicID) {
        for (Integer subTaskID : epics.get(epicID).getSubTasksID()) {
            subTasks.remove(subTaskID);
            historyManager.remove(subTaskID);
        }
        epics.remove(epicID);
        historyManager.remove(epicID);
    }

    @Override
    public void deleteSubTask(Integer subTaskID) {
        int epicID = subTasks.get(subTaskID).getEpicID();
        epics.get(epicID).deleteSubTaskID(subTaskID);
        subTasks.remove(subTaskID);
        setEpicStatus(epicID);
        historyManager.remove(subTaskID);
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getID());
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getID());
        }

        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getID());
        }

        epics.clear();
        subTasks.clear();

    }

    @Override
    public void clearSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getID());
        }

        subTasks.clear();

        for (Integer epicID : epics.keySet()) {
            if (!epics.get(epicID).getSubTasksID().isEmpty()) {
                epics.get(epicID).getSubTasksID().clear();
                epics.get(epicID).setStatus(Status.NEW);
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.replace(task.getID(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.replace(subTask.getID(), subTask);
        setEpicStatus(subTask.getEpicID());
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.replace(epic.getID(), epic);
    }

    public List<? extends Task> getHistory() {
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