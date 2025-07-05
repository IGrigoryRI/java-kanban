package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (!method.equals("GET")) {
            sendIncorrectRequest(exchange);
        } else {
            TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();


            if (prioritizedTasks.isEmpty()) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(prioritizedTasks));
            }
        }
    }
}