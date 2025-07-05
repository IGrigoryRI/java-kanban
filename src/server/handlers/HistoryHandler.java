package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (!method.equals("GET")) {
            sendIncorrectRequest(exchange);
        } else {
            List<? extends Task> history = taskManager.getHistory();

            if (history.isEmpty()) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(history));
            }
        }
    }
}