package TaskApp;

import java.util.ArrayList;

public class InMemoryHistoryManager<T extends TaskInterface> implements HistoryManager<T> {

    public ArrayList<T> history = new ArrayList<>();

    @Override
    public ArrayList<T> getHistory() {
        return history;
    }

    @Override
    public void addHistory(T task) {
        if (task == null) {
            return;
        }

        if (history.size() == 10) {
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }
    }
}