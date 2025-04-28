package TaskApp;

import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subTasksID = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubTasksID() {
        return subTasksID;
    }

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