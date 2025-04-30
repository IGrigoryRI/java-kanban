package TasksPackage;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected List<Integer> subTasksID = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubTasksID() {
        return subTasksID;
    }

    public void deleteSubTaskID(int subTaskID) {
        for (int i = 0; i < subTasksID.size(); i++) {
            if (subTasksID.get(i) == subTaskID) {
                subTasksID.remove(i);
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
                ", subTasksID=" + subTasksID +
                '}';
    }

    private Epic(Epic epic) {
        super(epic.getName(), epic.getDescription());
        this.status = epic.getStatus();
        this.ID = epic.getID();
        this.subTasksID = epic.getSubTasksID();
    }
}