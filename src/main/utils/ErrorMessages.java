package main.utils;

public class ErrorMessages {

    private ErrorMessages(){}

    public static String BAD_RESPONSE_BODY = "Во время выполнения запроса возникла ошибка.\n" +
            "Проверьте, пожалуйста, адрес и повторите попытку.";

    public static String BAD_STATUS_CODE = "Что-то пошло не так. Сервер вернул код состояния: ";
}
