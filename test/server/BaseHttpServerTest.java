package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.adapters.DurationAdapter;
import server.adapters.LocalDateTimeAdapter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import managers.TaskManager;
import managers.InMemoryTaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpServerTest {
    protected final TaskManager taskManager = new InMemoryTaskManager();
    protected final HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    protected BaseHttpServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.clearTasks();
        taskManager.clearSubTasks();
        taskManager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }
}