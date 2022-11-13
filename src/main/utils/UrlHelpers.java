package main.utils;

import main.exceptions.BadRequestException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UrlHelpers {
    private UrlHelpers() {
    }

    /**
     * Парсинг query строки в int
     *
     * @param query query строка
     * @return int
     */
    public static int parseQueryValueToInt(String query) {
        try {
            return Integer.parseInt(query);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Перевод query параметров в мапу ключ:значение
     *
     * @param query query строка
     * @return мапа ключ:значение
     */
    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) {
            return result;
        }
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    /**
     * Проверяет query параметр id на null
     *
     * @param id query значение уникального идентификатора
     * @return id == null
     */
    public static boolean checkForQueryIdParam(Integer id) throws BadRequestException {
        if (!Objects.isNull(id) && id == -1) {
            throw new BadRequestException();
        }

        return Objects.isNull(id);
    }
}
