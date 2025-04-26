package TaskApp;

public class SubTask extends Task {
    protected Integer epicID;

    public SubTask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }
}