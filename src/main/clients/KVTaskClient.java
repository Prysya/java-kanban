package main.clients;

import main.utils.ErrorMessages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    /**
     * Токен
     */
    private final String apiToken;
    /**
     * Url адрес сервера
     */
    private final String kvServerUrl;
    /**
     * Инстанс http клиента
     */
    private final HttpClient client;

    /**
     * Конструктор класса {@link KVTaskClient}
     *
     * @param url адрес сервера для работы с бэкапом менеджера
     */
    public KVTaskClient(String url) {
        kvServerUrl = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        client = HttpClient.newHttpClient();
        apiToken = getTokenResponse();

        System.out.println("API_TOKEN_IS: " + apiToken);
    }

    /**
     * Метод для отправки GET запроса на сервер
     *
     * @param uri ендпоинт для отправки запроса
     */
    private String makeGetResponse(URI uri) {
        try {
            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder().uri(uri).GET().build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                return response.body();
            } else {
                System.out.println(ErrorMessages.BAD_STATUS_CODE + response.statusCode());
            }
        }
        catch (IOException | InterruptedException | NullPointerException e) {
            System.out.println(ErrorMessages.BAD_RESPONSE_BODY);
        }

        return "";
    }

    /**
     * Метод для получения токена с сервера
     *
     * @return json с токеном
     */
    private String getTokenResponse() {
        URI uri = URI.create(kvServerUrl + "/register");
        return makeGetResponse(uri);
    }

    /**
     * Метод для сохранения данных по ключу на сервере
     *
     * @param key  ключ для сохранения
     * @param json данные для сохранения на сервере
     */
    public void put(String key, String json) {
        try {
            URI uri = URI.create(kvServerUrl + "/save/" + key + "?API_TOKEN=" + apiToken);
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Json по ключу " + key + " сохранен успешно!");
            } else {
                System.out.println(ErrorMessages.BAD_STATUS_CODE + response.statusCode());
            }
        }
        catch (IOException | InterruptedException e) {
            System.out.println(ErrorMessages.BAD_RESPONSE_BODY);
        }
    }

    /**
     * Метод для получения данных по ключу с сервера
     *
     * @param key ключ для получения данных
     * @return json с значением сохраненым по key
     */
    public String load(String key) {
        URI uri = URI.create(kvServerUrl + "/load/" + key + "?API_TOKEN=" + apiToken);
        return makeGetResponse(uri);
    }
}
