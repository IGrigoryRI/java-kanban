package tasks;

public class SubTask extends Task {
    protected Integer epicID;

    public SubTask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "TaskApp.Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicID=" + epicID +
                '}';
    }


    @Override
    public SubTask copy() {
        return new SubTask(this);
    }

    private SubTask(SubTask subTask) {
        super(subTask.getName(), subTask.getDescription());
        this.status = subTask.getStatus();
        this.ID = subTask.getID();
        this.epicID = subTask.getEpicID();
    }
}