package server.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Класс LocalDateTimeAdapter предоставляет адаптер для сериализации и десериализации объектов LocalDateTime.
 * Используется для работы с JSON-данными через Gson.
 *
 * @param FORMATTER - форматтер для преобразования LocalDateTime в строку и обратно.
 * @param writer - объект JsonWriter для записи данных.
 * @param reader - объект JsonReader для чтения данных.
 * @param localDateTime - объект LocalDateTime, который нужно сериализовать.
 * @throws IOException - если возникает ошибка при записи данных.
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter writer, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            writer.nullValue();
        } else {
            writer.value(FORMATTER.format(localDateTime));
        }
    }

    @Override
    public LocalDateTime read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        return LocalDateTime.parse(reader.nextString(), FORMATTER);
    }
}