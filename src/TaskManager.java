import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int id = 0;

    public void putNewTask(Task newTask) {
        newTask.setID(id);
        tasks.put(id, newTask);
        createID();
    }

    public void putNewEpic(Epic newEpic) {
        newEpic.setID(id);
        epics.put(id, newEpic);
        setEpicStatus(id);
        createID();
    }

    public void putNewSubTask(SubTask newSubTask) {
        newSubTask.setID(id);
        subTasks.put(id, newSubTask);
        int epicId = subTasks.get(id).getEpicID();
        setEpicStatus(epicId);
        epics.get(epicId).getSubTasksID().add(id);
        createID();
    }

    public ArrayList<Task> getAllTaskList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getAllSubTasksList() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getAllEpicsList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getEpicAllSubTasks(int epicID) {
        ArrayList<SubTask> epicsSubTasks = new ArrayList<>();
        for (Integer subTaskID : epics.get(epicID).getSubTasksID()) {
            epicsSubTasks.add(subTasks.get(subTaskID));
        }
        return epicsSubTasks;
    }

    public Task getTask(int taskID) {
        return tasks.get(taskID);
    }

    public Epic getEpic(int epicID) {
        return epics.get(epicID);
    }

    public SubTask getSubTask(int subTaskID) {
        return subTasks.get(subTaskID);
    }

    public void deleteTask(Integer taskID) {
        tasks.remove(taskID);
    }

    public void deleteEpic(Integer epicID) {
        for (Integer subTaskID : epics.get(epicID).getSubTasksID()) {
            subTasks.remove(subTaskID);
        }
        epics.remove(epicID);
    }

    public void deleteSubTask(Integer subTaskID) {
        int epicID = subTasks.get(subTaskID).getEpicID();
        epics.get(epicID).deleteSubTaskID(subTaskID);
        subTasks.remove(subTaskID);
        setEpicStatus(epicID);
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
        for (Integer epicID : epics.keySet()) {
            epics.get(epicID).subTasksID.clear();
            setEpicStatus(epicID);
        }
    }

    public void updateTask(Task task) {
        tasks.replace(task.getID(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.replace(subTask.getID(), subTask);
        setEpicStatus(subTask.getEpicID());
    }

    public void updateEpic(Epic epic) {
        epics.replace(epic.getID(), epic);
    }

    private void createID() {
        id++;
    }

    private void setEpicStatus(int epicID) {
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
            epics.get(epicID).setStatus(Status.NEW);
        } else if ((newCounter + inProgressCounter == 0) && (doneCounter > 0)) {
            epics.get(epicID).setStatus(Status.DONE);
        } else {
            epics.get(epicID).setStatus(Status.IN_PROGRESS);
        }
    }
}

