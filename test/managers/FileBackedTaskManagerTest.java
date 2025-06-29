package managers;

import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File tempFile;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager();
    }

    @BeforeEach
    void beforeEach() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        taskManager = createTaskManager();
    }

    @AfterEach
    void afterEach() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubTasks();
    }

    @Test
    void managerMustSaveAndLoadEmptyFile() {
        taskManager.putNewTask(new Task("Задача 1", "Описание 1",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5)));
        taskManager.deleteTask(0);
        taskManager = new FileBackedTaskManager();

        assertTrue(taskManager.getAll().isEmpty(), "Список задач должен быть пуст");
    }

    @Test
    void tasksShouldSaveAndLoad() {
        taskManager.putNewTask(new Task("Задача 1", "Описание 1",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5)));
        taskManager.putNewEpic(new Epic("Эпик 1", "Описание 2"));
        taskManager.putNewSubTask(new SubTask("Подзадача 1", "Описание 3",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 21, 5), 1));

        assertEquals(3, taskManager.getAll().size(), "Количество задач должно быть равно 3.");
        assertEquals("Задача 1", taskManager.getAll().get(0).getName(), "Задача 1 должна быть загружена.");
        assertEquals("Эпик 1", taskManager.getAll().get(1).getName(), "Задача 1 должна быть загружена.");
        assertEquals("Подзадача 1", taskManager.getAll().get(2).getName(), "Задача 1 должна быть загружена.");
    }

    @Test
    void testSaveAndLoadWithClearOperations() {
        taskManager.putNewTask(new Task("Задача 1", "Описание 1",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5)));
        taskManager.putNewEpic(new Epic("Эпик 1", "Описание 2"));
        taskManager.putNewSubTask(new SubTask("Подзадача 1", "Описание 3",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 21, 5), 1));

        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubTasks();

        taskManager = new FileBackedTaskManager();

        assertTrue(taskManager.getAll().isEmpty(), "После очистки список задач должен быть пуст");
    }
}