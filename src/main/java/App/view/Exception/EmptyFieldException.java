package App.view.Exception;

public class EmptyFieldException extends Exception{
    public EmptyFieldException() {
    }

    public EmptyFieldException(String message) {
        super(message);
    }
}
