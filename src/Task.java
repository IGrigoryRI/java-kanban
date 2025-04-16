public class Task {
    private String name;
    private String description;
    private Status status = Status.NEW;

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

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
