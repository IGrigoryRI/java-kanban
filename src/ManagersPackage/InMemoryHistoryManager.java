package ManagersPackage;

import TasksPackage.TaskInterface;

import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    public List<TaskInterface> history = new ArrayList<>();

    @Override
    public List<TaskInterface> getHistory() {
        return history;
    }

    @Override
    public void addHistory(TaskInterface task) {
        if (history.size() > 9) {
            history.removeFirst();
            history.add(task.copy());
        } else {
            history.add(task.copy());
        }
    }
}