package main.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ServerUtils {
    private ServerUtils() {
    }

    public static void sendResponse(HttpExchange httpExchange, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(rCode, resp.length);
        httpExchange.getResponseBody().write(resp);
    }

    public static void sendNotAllowedHeader(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
    }
}
