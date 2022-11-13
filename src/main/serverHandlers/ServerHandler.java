package main.serverHandlers;

import main.exceptions.BadRequestException;
import main.exceptions.MethodNotAllowedException;
import main.exceptions.NotFoundException;

/**
 * Интерфейс с методами обработчиков
 */
public interface ServerHandler<T> {
    /**
     * Обработчик GET метода, парсит вызваный элемент в строку
     *
     * @return отпарсеный json
     * @throws BadRequestException если query параметр id передан в не правильном формате
     * @throws NotFoundException   если по query параметру id не найдена задача
     */
    String getHandler() throws BadRequestException, NotFoundException;

    /**
     * Обработчик POST метода, парсит json в класс
     *
     * @param json в виде строки
     * @throws BadRequestException       если переданы не все поля класса конкретной задачи
     * @throws MethodNotAllowedException в случае отсутствия обработчика метода
     */
    void postHandler(String json) throws BadRequestException, MethodNotAllowedException;

    /**
     * Обработчик DELETE метода, удаляет элементы
     *
     * @throws BadRequestException       если query параметр id передан в не правильном формате
     * @throws NotFoundException         если по query параметру id не найдена задача
     * @throws MethodNotAllowedException в случае отсутствия обработчика метода
     */
    void deleteHandler() throws BadRequestException, NotFoundException, MethodNotAllowedException;
}
