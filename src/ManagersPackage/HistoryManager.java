package ManagersPackage;

import java.util.List;
import TasksPackage.*;

public interface HistoryManager {
    List<? extends Task> getHistory();

    void addHistory(Task task);
}
