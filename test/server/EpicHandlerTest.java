package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicHandlerTest extends BaseHttpServerTest {

    protected EpicHandlerTest() throws IOException {
    }

    @Test
    public void testAddEpic() throws Exception {
        Epic epic = new Epic("Эпик 0", "Описание 0"); //0
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Epic> epicsFromManager = taskManager.getAllEpicsList();
        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals(epic.getName(), epicsFromManager.get(0).getName(), "Некорректная запись названия задачи");

    }

    @Test
    public void testGetEpic() throws Exception {
        taskManager.putNewEpic(new Epic("Эпик 0", "Описание 0")); //0

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Epic> epicsFromResponse = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {}.getType());
        assertNotNull(epicsFromResponse, "Задачи не возвращаются");
        assertEquals(1, epicsFromResponse.size(), "Некорректное количество задач");

        assertEquals(taskManager.getEpic(0), epicsFromResponse.get(0), "Первая задача не совпадает");
    }

    @Test
    public void testGetAllEpics() throws Exception {
        taskManager.putNewEpic(new Epic("Эпик 0", "Описание 0")); //0
        taskManager.putNewEpic(new Epic("Эпик 1", "Описание 1")); //1

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Epic> epicsFromResponse = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {}.getType());
        assertNotNull(epicsFromResponse, "Задачи не возвращаются");
        assertEquals(2, epicsFromResponse.size(), "Некорректное количество задач");

        List<Epic> epicsFromManager = taskManager.getAllEpicsList();
        assertEquals(epicsFromManager.get(0), epicsFromResponse.get(0), "Первая задача не совпадает");
        assertEquals(epicsFromManager.get(1), epicsFromResponse.get(1), "Вторая задача не совпадает");
    }

    @Test
    public void testGetAllEpicSubTasks() throws Exception {
        taskManager.putNewEpic(new Epic("Эпик 0", "Описание 0")); //0
        taskManager.putNewSubTask(new SubTask("Подзадача 1", "Описание 1",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5), 0)); //1
        taskManager.putNewSubTask(new SubTask("Подзадача 2", "Описание 2",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 21, 5), 0)); //2

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics"+ taskManager.getEpic(0).getId() + "subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Epic> epicsFromResponse = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {}.getType());
        Epic epicFromResponse = epicsFromResponse.get(0);
        List<SubTask> subTasksFromResponse = epicFromResponse.getSubTasks();
        assertNotNull(subTasksFromResponse, "Подзадачи не возвращаются");
        assertEquals(2, subTasksFromResponse.size(), "Некорректное количество подзадач");

        List<SubTask> subTasksFromManager = taskManager.getEpicAllSubTasks(0);
        assertEquals(subTasksFromManager.get(0), subTasksFromResponse.get(0), "Первая подзадача не совпадает");
        assertEquals(subTasksFromManager.get(1), subTasksFromResponse.get(1), "Вторая подзадача не совпадает");
    }

    @Test
    public void testDeleteEpic() throws Exception {
        taskManager.putNewEpic(new Epic("Эпик 0", "Описание 0")); //0

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + taskManager.getEpic(0).getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").get();

        assertEquals(200, response.statusCode(), "Некорректный код статуса");
        assertEquals("application/json;charset=utf-8", contentType, "Некорректный Content-Type");

        List<Epic> epicsFromManager = taskManager.getAllEpicsList();
        assertTrue(epicsFromManager.isEmpty(), "Задача не была удалена");
    }
}