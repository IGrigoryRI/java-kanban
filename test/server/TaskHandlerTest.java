package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Duration;
import java.time.LocalDateTime;

import tasks.Task;

import java.io.IOException;
import java.util.List;

public class TaskHandlerTest extends BaseHttpServerTest {

    protected TaskHandlerTest() throws IOException {
    }

    @Test
    public void testAddTask() throws Exception {
        Task task = new Task("Задача 0", "Описание 0",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5)); //0
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Task> tasksFromManager = taskManager.getAllTaskList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(task.getName(), tasksFromManager.get(0).getName(), "Некорректная запись названия задачи");

    }

    @Test
    public void testGetTask() throws Exception {
        taskManager.putNewTask(new Task("Задача 0", "Описание 0",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5))); //0

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Task> tasksFromResponse = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(tasksFromResponse, "Задачи не возвращаются");
        assertEquals(1, tasksFromResponse.size(), "Некорректное количество задач");

        assertEquals(taskManager.getTask(0), tasksFromResponse.get(0), "Первая задача не совпадает");
    }

    @Test
    public void testGetAllTasks() throws Exception {
        taskManager.putNewTask(new Task("Задача 0", "Описание 0",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5))); //0
        taskManager.putNewTask(new Task("Задача 1", "Описание 1",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 21, 5))); //1

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Task> tasksFromResponse = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(tasksFromResponse, "Задачи не возвращаются");
        assertEquals(2, tasksFromResponse.size(), "Некорректное количество задач");

        List<Task> tasksFromManager = taskManager.getAllTaskList();
        assertEquals(tasksFromManager.get(0), tasksFromResponse.get(0), "Первая задача не совпадает");
        assertEquals(tasksFromManager.get(1), tasksFromResponse.get(1), "Вторая задача не совпадает");
    }

    @Test
    public void testDeleteTask() throws Exception {
        taskManager.putNewTask(new Task("Задача 0", "Описание 0",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5))); //0

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskManager.getTask(0).getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Task> tasksFromManager = taskManager.getAllTaskList();
        assertTrue(tasksFromManager.isEmpty(), "Задача не была удалена");
    }
}