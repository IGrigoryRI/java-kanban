package ManagersPackage;

import java.util.List;
import java.util.ArrayList;
import TasksPackage.*;

class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
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