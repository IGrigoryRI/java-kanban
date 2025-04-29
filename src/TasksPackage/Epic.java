package TasksPackage;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task implements EpicInterface {

    protected List<Integer> subTasksID = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public List<Integer> getSubTasksID() {
        return subTasksID;
    }

    @Override
    public void deleteSubTaskID(int subTaskID) {
        for (int i = 0; i < subTasksID.size(); i++) {
            if (subTasksID.get(i) == subTaskID) {
                subTasksID.remove(i);
            }
        }
    }

    @Override
    public Epic copy() {
        return new Epic(this);
    }

    @Override
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