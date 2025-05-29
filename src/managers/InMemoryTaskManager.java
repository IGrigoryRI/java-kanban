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
        newTask.setId(id);
        tasks.put(id, newTask);
        createID();
    }

    @Override
    public void putNewEpic(Epic newEpic) {
        newEpic.setId(id);
        epics.put(id, newEpic);
        setEpicStatus(id);
        createID();
    }

    @Override
    public void putNewSubTask(SubTask newSubTask) {
        newSubTask.setId(id);
        subTasks.put(id, newSubTask);
        int epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).getSubTasksId().add(id);
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
    public List<SubTask> getEpicAllSubTasks(int epicId) {
        List<SubTask> epicsSubTasks = new ArrayList<>();
        for (Integer subTaskId : epics.get(epicId).getSubTasksId()) {
            epicsSubTasks.add(subTasks.get(subTaskId));
        }
        return epicsSubTasks;
    }

    @Override
    public Task getTask(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        historyManager.add(subTasks.get(subTaskId));
        return subTasks.get(subTaskId);
    }

    @Override
    public void deleteTask(Integer taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(Integer epicId) {
        for (Integer subTaskId : epics.get(epicId).getSubTasksId()) {
            subTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        epics.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void deleteSubTask(Integer subTaskId) {
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).deleteSubTaskId(subTaskId);
        subTasks.remove(subTaskId);
        setEpicStatus(epicId);
        historyManager.remove(subTaskId);
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }

        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }

        epics.clear();
        subTasks.clear();

    }

    @Override
    public void clearSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }

        subTasks.clear();

        for (Integer epicId : epics.keySet()) {
            if (!epics.get(epicId).getSubTasksId().isEmpty()) {
                epics.get(epicId).getSubTasksId().clear();
                epics.get(epicId).setStatus(Status.NEW);
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.replace(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.replace(subTask.getId(), subTask);
        setEpicStatus(subTask.getEpicId());
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
    }

    public List<? extends Task> getHistory() {
        return historyManager.getHistory();
    }

    private void createID() {
        id++;
    }

    private void setEpicStatus(int epicId) {
        int newCounter = 0;
        int inProgressCounter = 0;
        int doneCounter = 0;

        for (int subTaskId : epics.get(epicId).getSubTasksId()) {
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
            epics.get(epicId).setStatus(Status.NEW);
        } else if ((newCounter + inProgressCounter == 0) && (doneCounter > 0)) {
            epics.get(epicId).setStatus(Status.DONE);
        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }
}