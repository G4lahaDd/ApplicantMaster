package App.model.service.exception;

/**
 * Исключение, вызываемое при ошибки выполнения в слое модели
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ServiceException extends Exception{
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }
}
