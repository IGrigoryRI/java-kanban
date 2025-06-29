package managers;

import tasks.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeAll
    public static void beforeAll() {
        taskManager.putNewTask(new Task("Задача 1", "Описание 1",
                Duration.ofHours(2),
                LocalDateTime.of(2023, 10, 1, 9, 0))); // 0
    }

    @BeforeEach
    public void beforeEach() {
        taskManager.getTask(0).setName("Задача 1");
        taskManager.getTask(0).setDescription("Описание 1");
        taskManager.getTask(0).setStatus(Status.NEW);
        taskManager.getTask(0).setId(0);
    }

    @Test
    public void shouldGetCorrectDescription() {
        String expectedDescription = "Описание 1";
        String actualDescription = taskManager.getTask(0).getDescription();
        assertEquals(expectedDescription, actualDescription, "Возвращается некорректное описание задачи");
    }

    @Test
    public void shouldSetCorrectDescription() {
        String expectedDescription = "Описание 2";
        taskManager.getTask(0).setDescription("Описание 2");
        String actualDescription = taskManager.getTask(0).getDescription();
        assertEquals(expectedDescription, actualDescription, "Описание задачи устанавливается некорректно");
    }

    @Test
    public void shouldGetCorrectStatus() {
        Status expectedStatus = Status.NEW;
        Status actualStatus = taskManager.getTask(0).getStatus();
        assertEquals(expectedStatus, actualStatus, "Возвращается некорректный статусзадачи ");
    }

    @Test
    public void shouldSetCorrectStatus() {
        Status expectedStatus = Status.DONE;
        taskManager.getTask(0).setStatus(Status.DONE);
        Status actualStatus = taskManager.getTask(0).getStatus();
        assertEquals(expectedStatus, actualStatus, "Статус задачи устанавливается некорректно");
    }

    @Test
    public void shouldGetCorrectName() {
        String expectedName = "Задача 1";
        String actualName = taskManager.getTask(0).getName();
        assertEquals(expectedName, actualName, "Возвращается некорректное наименование задачи");
    }

    @Test
    public void shouldSetCorrectName() {
        String expectedName = "Задача 2";
        taskManager.getTask(0).setName("Задача 2");
        String actualName = taskManager.getTask(0).getName();
        assertEquals(expectedName, actualName, "Наименование задачи устанавливается некорректно");
    }

    @Test
    public void shouldGetCorrectId() {
        Integer expectedId = 0;
        Integer actualId = taskManager.getTask(0).getId();
        assertEquals(expectedId, actualId, "Возвращается некорректное id задачи");
    }

    @Test
    public void shouldSetCorrectId() {
        Integer expectedId = 1;
        taskManager.getTask(0).setId(1);
        Integer actualId = taskManager.getTask(0).getId();
        assertEquals(expectedId, actualId, "Id задачи устанавливается некорректно");
    }
}