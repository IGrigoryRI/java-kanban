package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected List<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void deleteSubTaskId(int subTaskID) {
        for (int i = 0; i < subTasksId.size(); i++) {
            if (subTasksId.get(i) == subTaskID) {
                subTasksId.remove(i);
            }
        }
    }

    public Epic copy() {
        return new Epic(this);
    }

    public String toString() {
        return "TaskApp.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subTasksID=" + subTasksId +
                '}';
    }

    private Epic(Epic epic) {
        super(epic.getName(), epic.getDescription());
        this.status = epic.getStatus();
        this.id = epic.getId();
        this.subTasksId = epic.getSubTasksId();
    }
}