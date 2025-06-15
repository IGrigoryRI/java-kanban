package tasks;

import managers.TaskType;

public class SubTask extends Task {
    protected Integer epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
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
        this.taskType = subTask.getType();
    }
}