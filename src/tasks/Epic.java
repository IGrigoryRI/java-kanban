package tasks;

import managers.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {

    protected List<SubTask> subTasks = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.taskType = TaskType.EPIC;
        this.startTime = getStartTime();
        this.duration = getDuration();
        this.endTime = getEndTime();
    }

    @Override
    public Duration getDuration() {
        if (subTasks.isEmpty()) {
            return Duration.ZERO;
        }

        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;

        for (SubTask subTask : subTasks) {
            LocalDateTime subTaskStart = subTask.getStartTime();
            LocalDateTime subTaskEnd = subTask.getEndTime();

            if (subTaskStart != null && (earliestStart == null || subTaskStart.isBefore(earliestStart))) {
                earliestStart = subTaskStart;
            }

            if (subTaskEnd != null && (latestEnd == null || subTaskEnd.isAfter(latestEnd))) {
                latestEnd = subTaskEnd;
            }
        }

        if (earliestStart == null || latestEnd == null) {
            return Duration.ZERO;
        }

        return Duration.between(earliestStart, latestEnd);
    }

    public LocalDateTime getStartTime() {
        if (subTasks.isEmpty()) {
            return null;
        }

        LocalDateTime earliestStartTime = null;
        for (SubTask subTask : subTasks) {
            LocalDateTime subTaskStartTime = subTask.getStartTime();
            if (subTaskStartTime != null && (earliestStartTime == null
                    || subTaskStartTime.isBefore(earliestStartTime))) {
                earliestStartTime = subTaskStartTime;
            }
        }
        return earliestStartTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subTasks.isEmpty()) {
            return null;
        }

        LocalDateTime latestEndTime = null;
        for (SubTask subTask : subTasks) {
            LocalDateTime subTaskEndTime = subTask.getEndTime();
            if (subTaskEndTime != null && (latestEndTime == null
                    || subTaskEndTime.isAfter(latestEndTime))) {
                latestEndTime = subTaskEndTime;
            }
        }
        return latestEndTime;
    }

    @Override
    public Epic copy() {
        return new Epic(this);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        this.startTime = getStartTime();
        this.duration = getDuration();
        this.endTime = getEndTime();
    }

    public List<SubTask> getSubTasks() {
        if (subTasks.isEmpty()) {
            return new ArrayList<>();
        }
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    public String toString() {
        return "TaskApp.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", subTasks=" + subTasks +
                '}' + "\n";
    }

    private Epic(Epic epic) {
        super(epic.getName(), epic.getDescription());
        this.status = epic.getStatus();
        this.id = epic.getId();
        this.subTasks = epic.getSubTasks();
        this.taskType = epic.getType();
        this.duration = epic.getDuration();
        this.startTime = epic.getStartTime();
    }
}