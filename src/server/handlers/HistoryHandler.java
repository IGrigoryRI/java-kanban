package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import server.HttpMethod;
import tasks.Task;

import java.io.IOException;
import java.util.List;

/*
 * Класс HistoryHandler обрабатывает HTTP-запросы для получения истории просмотра задач.
 * Поддерживаемый эндпоинт:
 * - GET /history: Возвращает историю просмотра задач.
 *
 * @param taskManager - менеджер задач, который будет использоваться для операций с задачами.
 * @param exchange - объект HttpExchange, содержащий информацию о запросе и ответе.
 * @throws IOException если возникает ошибка при чтении или записи данных.
 */
public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());

        if (!method.equals(HttpMethod.GET)) {
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