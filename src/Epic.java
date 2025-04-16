import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksID = new ArrayList<>();

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
}

// Прописать получение эпиком всех ID его SubTask-ов