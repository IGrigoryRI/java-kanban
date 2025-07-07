package managers;

import java.util.List;

import tasks.Task;

public interface HistoryManager {
    List<? extends Task> getHistory();

    void add(Task task);

    void remove(int id);
}
