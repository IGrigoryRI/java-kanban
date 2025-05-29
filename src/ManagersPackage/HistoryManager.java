package ManagersPackage;

import java.util.List;
import java.util.Map;
import TasksPackage.*;

public interface HistoryManager {
    List<? extends Task> getHistory();
    void add(Task task);
    void remove(int id);

}
