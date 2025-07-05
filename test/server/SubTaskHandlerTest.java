package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Duration;
import java.time.LocalDateTime;

import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.util.List;

public class SubTaskHandlerTest extends BaseHttpServerTest {
    protected SubTaskHandlerTest() throws IOException {
    }

    @Test
    public void testAddSubTask() throws Exception {
        taskManager.putNewEpic(new Epic("Эпик 0", "Описание 0")); //0
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5), 0); //1
        String subTaskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<SubTask> subTasksFromManager = taskManager.getAllSubTasksList();
        assertNotNull(subTasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals(subTask.getName(), subTasksFromManager.get(0).getName(), "Некорректная запись названия подзадачи");

    }

    @Test
    public void testGetSubTask() throws Exception {
        taskManager.putNewEpic(new Epic("Эпик 0", "Описание 0")); //0
        taskManager.putNewSubTask(new SubTask("Подзадача 1", "Описание 1",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5), 0)); //1

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<SubTask> subTasksFromResponse = gson.fromJson(response.body(),
                new TypeToken<List<SubTask>>() {}.getType());
        assertNotNull(subTasksFromResponse, "Подзадачи не возвращаются");
        assertEquals(1, subTasksFromResponse.size(), "Некорректное количество подзадач");

        assertEquals(taskManager.getSubTask(1), subTasksFromResponse.get(0), "Первая подзадача не совпадает");
    }

    @Test
    public void testGetAllSubTasks() throws Exception {
        taskManager.putNewEpic(new Epic("Эпик 0", "Описание 0")); //0
        taskManager.putNewSubTask(new SubTask("Подзадача 1", "Описание 1",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5), 0)); //1
        taskManager.putNewSubTask(new SubTask("Подзадача 2", "Описание 2",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 21, 5), 0)); //2

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<SubTask> subTasksFromResponse = gson.fromJson(response.body(),
                new TypeToken<List<SubTask>>() {}.getType());
        assertNotNull(subTasksFromResponse, "Подзадачи не возвращаются");
        assertEquals(2, subTasksFromResponse.size(), "Некорректное количество подзадач");

        List<SubTask> subTasksFromManager = taskManager.getAllSubTasksList();
        assertEquals(subTasksFromManager.get(0), subTasksFromResponse.get(0), "Первая подзадача не совпадает");
        assertEquals(subTasksFromManager.get(1), subTasksFromResponse.get(1), "Вторая подзадача не совпадает");
    }

    @Test
    public void testDeleteSubTask() throws Exception {
        taskManager.putNewEpic(new Epic("Эпик 0", "Описание 0")); //0
        taskManager.putNewSubTask(new SubTask("Подзадача 1", "Описание 1",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5), 0)); //1

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + taskManager.getSubTask(1).getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<SubTask> subTasksFromManager = taskManager.getAllSubTasksList();
        assertTrue(subTasksFromManager.isEmpty(), "Подзадача не была удалена");
    }
}