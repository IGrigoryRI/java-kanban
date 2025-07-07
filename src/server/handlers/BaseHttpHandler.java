package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import server.adapters.DurationAdapter;
import server.adapters.LocalDateTimeAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

/*
 * Класс BaseHttpHandler предоставляет базовую функциональность для обработчиков HTTP-запросов.
 * Содержит методы для отправки ответов и настройки Gson для сериализации/десериализации данных.
 *
 * @param gson - экземпляр Gson, сконфигурированный для работы с LocalDateTime и Duration.
 * @param exchange - объект HttpExchange, содержащий информацию о запросе и ответе.
 * @param text - текст ответа, который будет отправлен клиенту.
 * @throws IOException если возникает ошибка при записи данных.
 */
public class BaseHttpHandler {
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    /*
     * Отправляет успешный ответ (статус 200) с текстом в формате JSON.
     */
    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    /*
     * Отправляет ответ с кодом 404 (Not Found), если запрашиваемый объект не найден.
     */
    protected void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] response = "Объект не был найден".getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    /*
     * Отправляет ответ с кодом 405 (Method Not Allowed), если запрос некорректен.
     */
    protected void sendIncorrectRequest(HttpExchange exchange) throws IOException {
        byte[] response = "Некорректный запрос".getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(405, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    /*
     * Отправляет ответ с кодом 406 (Not Acceptable), если задача пересекается с существующей.
     */
    protected void sendHasOverlaps(HttpExchange exchange) throws IOException {
        byte[] response = "Задача пересекается с уже существующей".getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    /*
     * Отправляет ответ с кодом 500 (Internal Server Error), если произошла ошибка сервера.
     */
    protected void sendServerError(HttpExchange exchange) throws IOException {
        byte[] response = "Ошибка сервера".getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(500, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}