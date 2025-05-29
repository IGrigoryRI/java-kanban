package tests;

import managers.*;
import tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    public void beforeEach() {
        taskManager.putNewTask(new Task("Задача 1", "Описание")); // 0
        taskManager.putNewTask(new Task("Задача 2", "Описание")); // 1
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