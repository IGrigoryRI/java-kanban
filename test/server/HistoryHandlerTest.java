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
import java.util.List;

public class HistoryHandlerTest extends BaseHttpServerTest {

    protected HistoryHandlerTest() throws IOException {
    }

    @Test
    public void testGetHistory() throws Exception {
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

        taskManager.getSubTask(3);
        taskManager.getEpic(2);
        taskManager.getTask(1);
        taskManager.getTask(0);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Некорректный код статуса");

        List<Task> history = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(history, "История не возвращается");
        assertEquals(4, history.size(), "Некорректное количество задач в истории");

        assertEquals(taskManager.getSubTask(3).getName(), history.get(0).getName(), "Первая задача в истории неверна");
        assertEquals(taskManager.getEpic(2).getName(), history.get(1).getName(),"Вторая задача в истории неверна");
        assertEquals(taskManager.getTask(1).getName(), history.get(2).getName(),"Третья задача в истории неверна");
        assertEquals(taskManager.getTask(0).getName(), history.get(3).getName(),"Четвертая задача в истории неверна");
    }
}