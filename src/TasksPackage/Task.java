package TasksPackage;

import ManagersPackage.Status;

import java.util.Objects;

public class Task implements TaskInterface {
    protected String name;
    protected String description;
    protected Status status = Status.NEW;
    protected Integer ID;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String taskDescription) {
        this.description = taskDescription;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Override
    public Task copy() {
        return new Task(this);
    }

    @Override
    public String toString() {
        return "TaskApp.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(ID, task.ID);
    }

    @Override
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
