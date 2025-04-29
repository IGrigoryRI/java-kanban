package ManagersPackage;

import TasksPackage.Task;
import TasksPackage.TaskInterface;

import java.util.List;

public interface HistoryManager {
    List<? extends TaskInterface> getHistory();

    void addHistory(TaskInterface task);
}
