package server.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

/*
 * Класс DurationAdapter предоставляет адаптер для сериализации и десериализации объектов Duration.
 * Используется для работы с JSON-данными через Gson.
 *
 * @param writer - объект JsonWriter для записи данных.
 * @param reader - объект JsonReader для чтения данных.
 * @param value - объект Duration, который нужно сериализовать.
 * @throws IOException если возникает ошибка при записи данных.
 */
public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter writer, Duration value) throws IOException {
        if (value == null) {
            writer.nullValue();
        } else {
            writer.value(value.toString());
        }
    }

    @Override
    public Duration read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        return Duration.parse(reader.nextString());
    }
}