package TaskApp;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    public ArrayList<Task> history = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    @Override
    public void addHistory(Task task) {
        if (history.size() > 9) {
            history.removeFirst();
            history.add(task.copy());
        } else {
            history.add(task.copy());
        }
    }
}