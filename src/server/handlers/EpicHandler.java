package server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                        List<Epic> epics = taskManager.getAllEpicsList();
                        sendText(exchange, gson.toJson(epics));

                        if (!epics.isEmpty()) {
                            sendText(exchange, gson.toJson(epics));
                        } else {
                            sendNotFound(exchange);
                        }

                    }

                    if (path.length == 3) {
                        int epicId = Integer.parseInt(path[2]);
                        Epic epic = taskManager.getEpic(epicId);

                        if (epic != null) {
                            sendText(exchange, gson.toJson(epic));
                        } else {
                            sendNotFound(exchange);
                        }
                    }

                    if (path.length == 4 && path[3].equals("subtasks")) {
                        int epicId = Integer.parseInt(path[2]);
                        List<SubTask> epics = taskManager.getEpicAllSubTasks(epicId);

                        if (!epics.isEmpty()) {
                            sendText(exchange, gson.toJson(epics));
                        } else {
                            sendNotFound(exchange);
                        }
                    }

                    if (path.length < 1 || path.length > 4) {
                        sendIncorrectRequest(exchange);
                    }

                } catch (NumberFormatException e) {
                    sendServerError(exchange);
                }
                break;

            case "POST":
                String body = new String(exchange.getRequestBody().readAllBytes());
                JsonElement jsonElement = JsonParser.parseString(body);
                JsonObject jsonEpic = jsonElement.getAsJsonObject();

                if (!jsonEpic.has("name") || !jsonEpic.has("description")) {
                    sendIncorrectRequest(exchange);
                }

                String name = jsonEpic.get("name").getAsString();
                String description = jsonEpic.get("description").getAsString();

                Epic epic = new Epic(name, description);

                if (path.length == 2) {
                    try {
                        taskManager.putNewEpic(epic);
                        sendText(exchange, "Эпик успешно добавлен");
                    } catch (IllegalArgumentException e) {
                        sendHasOverlaps(exchange);
                    }
                } else {
                    sendIncorrectRequest(exchange);
                }
                break;

            case "DELETE":
                try {
                    if (path.length == 2) {
                        taskManager.clearEpics();
                        sendText(exchange, "Все эпики успешно удалены");

                    } else if (path.length == 3) {
                        try {
                            int epicId = Integer.parseInt(path[2]);
                            taskManager.deleteEpic(epicId);
                            boolean isDeleted = taskManager.getEpic(epicId) == null;
                            if (isDeleted) {
                                sendText(exchange, "Эпик успешно удален");
                            } else {
                                sendNotFound(exchange);
                            }
                        } catch (NumberFormatException e) {
                            sendIncorrectRequest(exchange);
                        }
                    } else {
                        sendIncorrectRequest(exchange);
                    }
                } catch (Exception e) {
                    sendServerError(exchange);
                }
                break;

            default:
                sendIncorrectRequest(exchange);
        }
    }
}