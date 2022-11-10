package main.utils;

import com.google.gson.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GsonBuilders {
    private GsonBuilders() {
    }

    public static Gson getGsonWithLocalDateTime() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (value, type, context) ->
                new JsonPrimitive(value.format(DateTimeFormatter.ISO_DATE_TIME))
            )
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, context) ->
                LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_DATE_TIME)
            )
            .create();
    }
}
