package server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                try {
                    if (path.length == 2) {
                        List<Task> tasks = taskManager.getAllTaskList();
                        sendText(exchange, gson.toJson(tasks));
                    } else {
                        int taskId = Integer.parseInt(path[2]);
                        Task task = taskManager.getTask(taskId);

                        if (task != null) {
                            sendText(exchange, gson.toJson(task));
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                } catch (NumberFormatException e) {
                    sendIncorrectRequest(exchange);
                }
                break;

            case "POST":
                String body = new String(exchange.getRequestBody().readAllBytes());
                JsonElement jsonElement = JsonParser.parseString(body);
                JsonObject jsonTask = jsonElement.getAsJsonObject();

                if (!jsonTask.has("name") ||
                        !jsonTask.has("description") ||
                        !jsonTask.has("duration") ||
                        !jsonTask.has("startTime")) {
                    sendIncorrectRequest(exchange);
                    return;
                }

                String name = jsonTask.get("name").getAsString();
                String description = jsonTask.get("description").getAsString();
                Duration duration = Duration.parse(jsonTask.get("duration").getAsString());
                LocalDateTime startTime = LocalDateTime.parse(jsonTask.get("startTime").getAsString());

                Task task = new Task(name, description, duration, startTime);

                if (path.length == 2) {
                    try {
                        taskManager.putNewTask(task);
                        sendText(exchange, "Задача успешно добавлена");
                    } catch (IllegalArgumentException e) {
                        sendHasOverlaps(exchange);
                    }
                } else {
                    try {
                        int taskId = Integer.parseInt(path[2]);
                        task.setId(taskId);

                        try {
                            taskManager.updateTask(task);
                            sendText(exchange, "Задача успешно обновлена");
                        } catch (IllegalArgumentException e) {
                            sendHasOverlaps(exchange);
                        }
                    } catch (NumberFormatException e) {
                        sendIncorrectRequest(exchange);
                    }

                }
                break;

            case "DELETE":
                if (path.length == 2) {
                    taskManager.clearTasks();
                    sendText(exchange, "Все задачи успешно удалены");
                } else {
                    try {
                        int taskId = Integer.parseInt(path[2]);
                        taskManager.deleteTask(taskId);
                        sendText(exchange, "Задача успешно удалена");
                    } catch (NumberFormatException e) {
                        sendIncorrectRequest(exchange);
                    }
                }
                break;

            default:
                sendIncorrectRequest(exchange);
        }
    }
}