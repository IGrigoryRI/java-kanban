package TaskApp;

import java.util.ArrayList;

public interface HistoryManager<T extends TaskInterface> {
    ArrayList<T> getHistory();

    void addHistory(T task);
}
