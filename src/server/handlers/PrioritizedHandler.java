package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import server.HttpMethod;
import tasks.Task;

import java.io.IOException;
import java.util.TreeSet;

/*
 * Класс PrioritizedHandler обрабатывает HTTP-запросы для получения приоритизированного списка задач.
 * Поддерживаемый эндпоинт:
 * - GET /prioritized: Возвращает отсортированный список задач по времени начала выполнения.
 *
 * @param taskManager - менеджер задач, который будет использоваться для операций с задачами.
 * @param exchange - объект HttpExchange, содержащий информацию о запросе и ответе.
 * @throws IOException если возникает ошибка при чтении или записи данных.
 */
public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());

        if (!method.equals(HttpMethod.GET)) {
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