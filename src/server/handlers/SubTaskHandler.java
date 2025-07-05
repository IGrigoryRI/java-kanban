package server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubTaskHandler(TaskManager taskManager) {
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
                        List<SubTask> subTasks = taskManager.getAllSubTasksList();
                        sendText(exchange, gson.toJson(subTasks));

                        if (!subTasks.isEmpty()) {
                            sendText(exchange, gson.toJson(subTasks));
                        } else {
                            sendNotFound(exchange);
                        }

                    } else {
                        int taskId = Integer.parseInt(path[2]);
                        SubTask subTask = taskManager.getSubTask(taskId);

                        if (subTask != null) {
                            sendText(exchange, gson.toJson(subTask));
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
                JsonObject jsonSubTask = jsonElement.getAsJsonObject();

                if (!jsonSubTask.has("name") ||
                        !jsonSubTask.has("description") ||
                        !jsonSubTask.has("duration") ||
                        !jsonSubTask.has("startTime") ||
                        !jsonSubTask.has("epicId")) {
                    sendIncorrectRequest(exchange);
                    return;
                }

                String name = jsonSubTask.get("name").getAsString();
                String description = jsonSubTask.get("description").getAsString();
                Duration duration = Duration.parse(jsonSubTask.get("duration").getAsString());
                LocalDateTime startTime = LocalDateTime.parse(jsonSubTask.get("startTime").getAsString());
                int epicId = jsonSubTask.get("epicId").getAsInt();

                SubTask subTask = new SubTask(name, description, duration, startTime, epicId);

                if (path.length == 2) {
                    try {
                        taskManager.putNewSubTask(subTask);
                        sendText(exchange, "Подзадача успешно добавлена");
                    } catch (IllegalArgumentException e) {
                        sendHasOverlaps(exchange);
                    }
                } else {
                    try {
                        int subTaskId = Integer.parseInt(path[2]);
                        subTask.setId(subTaskId);

                        try {
                            taskManager.updateSubTask(subTask);
                            sendText(exchange, "Подзадача успешно обновлена");
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
                    taskManager.clearSubTasks();
                    sendText(exchange, "Все подзадачи успешно удалены");
                } else {
                    try {
                        int subTaskId = Integer.parseInt(path[2]);
                        taskManager.deleteSubTask(subTaskId);
                        sendText(exchange, "Подзадача успешно удалена");
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