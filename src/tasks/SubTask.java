package tasks;

public class SubTask extends Task {
    protected Integer epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "TaskApp.Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicID=" + epicId +
                '}';
    }


    @Override
    public SubTask copy() {
        return new SubTask(this);
    }

    private SubTask(SubTask subTask) {
        super(subTask.getName(), subTask.getDescription());
        this.status = subTask.getStatus();
        this.id = subTask.getId();
        this.epicId = subTask.getEpicId();
    }
}