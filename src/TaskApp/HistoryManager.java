package TaskApp;

import java.util.ArrayList;

public interface HistoryManager {
    ArrayList<? extends Task> getHistory();

    void addHistory(Task task);
}
