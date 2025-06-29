package tasks;

import managers.Status;
import managers.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected String name;
    protected String description;
    protected Status status = Status.NEW;
    protected Integer id;
    protected TaskType taskType = TaskType.TASK;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = getEndTime();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String taskDescription) {
        this.description = taskDescription;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Task copy() {
        return new Task(this);
    }

    public TaskType getType() {
        return this.taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public String toString() {
        return "TaskApp.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}' + "\n";
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status &&
                Objects.equals(id, task.id);
    }

    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    @Override
    public int compareTo(Task task) {
        int result = this.startTime.compareTo(task.startTime);
        if (result != 0) {
            return result;
        }
        return Integer.compare(this.id, task.id);
    }

    protected Task(String name, String description) {
        this.name= name;
        this.description = description;
    }

    private Task(Task task) {
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.id = task.getId();
        this.taskType = task.getType();
        this.duration = task.getDuration();
        this.startTime = task.getStartTime();
    }
}
