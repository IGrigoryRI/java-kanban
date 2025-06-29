package tasks;

import managers.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    protected Integer epicId;

    public SubTask(String name, String description, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public SubTask copy() {
        return new SubTask(this);
    }

    @Override
    public String toString() {
        return "TaskApp.Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", epicID=" + epicId +
                '}' + "\n";
    }

    private SubTask(SubTask subTask) {
        super(subTask.getName(), subTask.getDescription(), subTask.getDuration(), subTask.getStartTime());
        this.status = subTask.getStatus();
        this.id = subTask.getId();
        this.epicId = subTask.getEpicId();
        this.taskType = subTask.getType();
        this.duration = subTask.getDuration();
        this.startTime = subTask.getStartTime();
    }
}