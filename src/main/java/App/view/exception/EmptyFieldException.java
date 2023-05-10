package App.view.exception;

/**
 * Исключение, вызываемое при пустом поле
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class EmptyFieldException extends Exception{
    public EmptyFieldException() {
    }

    public EmptyFieldException(String message) {
        super(message);
    }
}
