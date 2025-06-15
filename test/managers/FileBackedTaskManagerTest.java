package managers;

import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private File tempFile;
    private FileBackedTaskManager taskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        taskManager = new FileBackedTaskManager();
    }

    @AfterEach
    void afterEach() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void managerMustSaveAndLoadEmptyFile() {
        taskManager.save();
        taskManager.load();

        assertTrue(taskManager.getAll().isEmpty(), "Список задач должен быть пуст");
    }

    @Test
    void tasksShouldSaveAndLoad() {
        taskManager.putNewTask(new Task("Задача 1", "Описание 1"));
        taskManager.putNewEpic(new Epic("Эпик 1", "Описание 2"));
        taskManager.putNewSubTask(new SubTask("Подзадача 1", "Описание 3", 1));

        FileBackedTaskManager newManager = new FileBackedTaskManager();

        List<Task> loadedTasks = newManager.getAll();

        assertEquals(3, loadedTasks.size(), "Количество задач должно быть равно 3.");
        assertEquals("Задача 1", loadedTasks.get(0).getName(), "Задача 1 должна быть загружена.");
        assertEquals("Эпик 1", loadedTasks.get(1).getName(), "Задача 1 должна быть загружена.");
        assertEquals("Подзадача 1", loadedTasks.get(2).getName(), "Задача 1 должна быть загружена.");
    }

    @Test
    void testSaveAndLoadWithClearOperations() {
        taskManager.putNewTask(new Task("Задача 1", "Описание 1"));
        taskManager.putNewEpic(new Epic("Эпик 1", "Описание 2"));
        taskManager.putNewSubTask(new SubTask("Подзадача 1", "Описание 3", 1));

        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubTasks();

        taskManager.load();

        assertTrue(taskManager.getAll().isEmpty(), "После очистки список задач должен быть пуст");
    }
}