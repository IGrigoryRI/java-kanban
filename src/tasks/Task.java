package tasks;

import managers.Status;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Status status = Status.NEW;
    protected Integer ID;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
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

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Task copy() {
        return new Task(this);
    }

    public String toString() {
        return "TaskApp.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(ID, task.ID);
    }

    public int hashCode() {
        return Objects.hash(name, description, status, ID);
    }

    private Task(Task task) {
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.ID = task.getID();
    }
}
