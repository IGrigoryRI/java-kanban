package managers;

import tasks.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.time.LocalDateTime;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>();
    private int id = 0;

    @Override
    public void putNewTask(Task newTask) {
        newTask.setId(id);

        if (isTaskHaveIntersection(newTask)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей.");
        }

        tasks.put(id, newTask);
        addToPrioritizedTasks(newTask);
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

        if (isTaskHaveIntersection(newSubTask)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей.");
        }

        subTasks.put(id, newSubTask);
        addToPrioritizedTasks(newSubTask);
        int epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).addSubTask(newSubTask);
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
        return epics.get(epicId).getSubTasks();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
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
        prioritizedTasks.remove(tasks.get(taskId));
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        epic.getSubTasks().stream()
                .peek(subTask -> subTasks.remove(subTask.getId()))
                .peek(subTask -> prioritizedTasks.remove(subTask))
                .forEach(subTask -> historyManager.remove(subTask.getId()));

        epics.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void deleteSubTask(Integer subTaskId) {
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).removeSubTask(subTasks.get(subTaskId));
        prioritizedTasks.remove(subTasks.get(subTaskId));
        subTasks.remove(subTaskId);
        setEpicStatus(epicId);
        historyManager.remove(subTaskId);
    }

    @Override
    public void clearTasks() {
        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });

        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.values().forEach(epic -> {
            historyManager.remove(epic.getId());
        });

        clearSubTasks();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        }

        subTasks.clear();

        for (Integer epicId : epics.keySet()) {
            if (!epics.get(epicId).getSubTasks().isEmpty()) {
                epics.get(epicId).getSubTasks().clear();
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

    protected List<Task> getAll() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subTasks.values());
        return allTasks;
    }

    protected void loadAll(List<Task> allTasks) {
        for (Task task : allTasks) {
            switch (task.getType()) {
                case TASK -> tasks.put(task.getId(), task);
                case SUBTASK -> {
                    SubTask subTask = (SubTask) task;
                    subTasks.put(task.getId(), subTask);
                    if (!epics.isEmpty()) {
                        epics.get(subTask.getEpicId()).addSubTask(subTask);
                    }
                }
                case EPIC -> epics.put(task.getId(), (Epic) task);
            }
        }
    }

    private void createID() {
        id++;
    }

    private void setEpicStatus(int epicId) {
        int newCounter = 0;
        int inProgressCounter = 0;
        int doneCounter = 0;

        for (SubTask subTask : epics.get(epicId).getSubTasks()) {
            int subTaskId = subTask.getId();
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

    private void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() != null && !task.getType().equals(TaskType.EPIC)) {
            prioritizedTasks.add(task);
        }
    }

    private boolean isTasksOverlap(Task taskOne, Task taskTwo) {
        LocalDateTime start1 = taskOne.getStartTime();
        LocalDateTime end1 = taskOne.getEndTime();
        LocalDateTime start2 = taskTwo.getStartTime();
        LocalDateTime end2 = taskTwo.getEndTime();

        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    private boolean isTaskHaveIntersection(Task task) {
        return prioritizedTasks.stream()
                .anyMatch(existingTask -> isTasksOverlap(task, existingTask));
    }
}