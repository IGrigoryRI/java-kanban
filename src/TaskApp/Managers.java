package TaskApp;

public final class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager<? extends TaskInterface> getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
