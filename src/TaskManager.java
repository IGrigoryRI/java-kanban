import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int ID = 0;

    private void createID() {
        ID++;
    }

    public void putNewTask(Task newTask) {
        tasks.put(ID, newTask);
        newTask.setID(ID);
        createID();
    }

    public void putNewEpic(Epic newEpic) {
        epics.put(ID, newEpic);
        newEpic.setID(ID);
        setEpicStatus(ID);
        createID();
    }

    public void putNewSubTask(SubTask newSubTask) {
        if (epics.containsKey(newSubTask.getEpicID())) {
            subTasks.put(ID, newSubTask);
            int epicID = subTasks.get(ID).getEpicID();
            setEpicStatus(epicID);
            epics.get(epicID).getSubTasksID().add(ID);
            newSubTask.setID(ID);
            createID();
        }
    }

    public ArrayList<Task> getAllTaskList() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }

    public ArrayList<SubTask> getAllSubTasksList() {
        ArrayList<SubTask> subtasksList = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            subtasksList.add(subTask);
        }
        return subtasksList;
    }

    public ArrayList<Epic> getAllEpicsList() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    public ArrayList<SubTask> getEpicAllSubTasks(int epicID) {
        ArrayList<SubTask> epicsSubTasks = new ArrayList<>();
        if (epics.containsKey(epicID)) {
            for (SubTask subTask : subTasks.values()) {
                if (subTask.getEpicID() == epicID) {
                    epicsSubTasks.add(subTask);
                }
            }
        }
        return epicsSubTasks;
    }

    public Task getTaskByID(int taskID) {
        Task task = tasks.get(taskID);
        return task;
    }

    public Epic getEpicByID(int epicID) {
        Epic epic = epics.get(epicID);
        return epic;
    }

    public SubTask getSubTaskByID(int subTaskID) {
        SubTask subTask = subTasks.get(subTaskID);
        return subTask;
    }

    public void deleteTask(Task task) {
        tasks.remove(task.getID());
    }

    public void deleteEpic(Epic epic) {
        epics.remove(epic.getID());
        for (Integer subID : subTasks.keySet()) {
            if (Objects.equals(subTasks.get(subID).getEpicID(), epic.getID())) {
                subTasks.remove(subID);
            }
        }
    }

    public void deleteSubTask(SubTask subTask) {
        int epicID = subTasks.get(subTask.getID()).getEpicID();
        epics.get(epicID).deleteSubTaskID(subTask.getID());
        subTasks.remove(subTask.getID());
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
            setEpicStatus(epicID);
            epics.get(epicID).subTasksID.clear();
        }
    }

    public void updateTask(Task task) {
        tasks.replace(task.getID(), task);
    }

    public void updateTaskStatus(Task task) {
        tasks.get(task.getID()).setStatus(Status.DONE);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.replace(subTask.getID(), subTask);
        setEpicStatus(subTasks.get(subTask.getID()).getEpicID());
    }

    public void updateSubTaskStatus(SubTask subTask) {
        subTasks.get(subTask.getID()).setStatus(Status.DONE);
        setEpicStatus(subTasks.get(subTask.getID()).getEpicID());
    }

    public void updateEpic(Epic epic) {
        epics.replace(epic.getID(), epic);
        setEpicStatus(epic.getID());
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

