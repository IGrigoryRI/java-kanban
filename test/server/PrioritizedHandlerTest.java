package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class PrioritizedHandlerTest extends BaseHttpServerTest {

    protected PrioritizedHandlerTest() throws IOException {
    }

    @Test
    public void testGetPrioritizedTasks() throws Exception {
        taskManager.putNewTask(new Task("Задача 0", "Описание 0",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5))); //0
        taskManager.putNewTask(new Task("Задача 0", "Описание 0",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 21, 5))); //1
        taskManager.putNewEpic(new Epic("Эпик 2", "Описание 2")); //2
        taskManager.putNewSubTask(new SubTask("Подзадача 3", "Описание 3",
                Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 12, 5), 2)); //3

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Некорректный код статуса");

        Set<Task> prioritizedSet = gson.fromJson(response.body(), new TypeToken<Set<Task>>() {}.getType());
        List<Task> prioritizedTasks = new ArrayList<>(prioritizedSet);
        assertNotNull(prioritizedSet, "Список не возвращается");
        assertEquals(3, prioritizedSet.size(), "Некорректное количество задач в списке");

        assertEquals(taskManager.getSubTask(3).getName(), prioritizedTasks.get(0).getName(),
                "Первая задача в списке неверна");
        assertEquals(taskManager.getTask(1).getName(), prioritizedTasks.get(1).getName(),
                "Вторая задача в списке неверна");
        assertEquals(taskManager.getTask(0).getName(), prioritizedTasks.get(2).getName(),
                "Третья задача в списке неверна");
    }
}