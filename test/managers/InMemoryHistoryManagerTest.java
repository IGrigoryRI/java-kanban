package managers;

import tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    public void beforeEach() {
        taskManager.putNewTask(new Task("Task 1", "Description",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5))); // 0
        taskManager.putNewTask(new Task("Task 1", "Description",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 21, 5))); // 1
        taskManager.getTask(0);
        taskManager.getTask(1);
    }

    @AfterEach
    public void afterEach() {
        taskManager.clearTasks();
        taskManager.clearSubTasks();
        taskManager.clearEpics();
    }

    @Test
    public void shouldAddTaskToHistory() {
        List<? extends Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать две задачи");
        assertEquals(taskManager.getTask(0), history.get(0), "Первая задача должна быть 'Задача 1'");
        assertEquals(taskManager.getTask(1), history.get(1), "Вторая задача должна быть 'Задача 2'");
    }

    @Test
    public void shouldRemoveDuplicatesFromHistory() {
        taskManager.getTask(0);

        List<? extends Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать две задачи");
        assertEquals(taskManager.getTask(1), history.get(0), "Первая задача должна быть 'Задача 1'");
        assertEquals(taskManager.getTask(0), history.get(1), "Вторая задача должна быть 'Задача 2'");
    }

    @Test
    public void shouldRemoveTaskFromHistory() {
        taskManager.clearTasks();

        List<? extends Task> history = taskManager.getHistory();
        assertEquals(0, history.size(), "История не должна содержать задачи");
    }
}