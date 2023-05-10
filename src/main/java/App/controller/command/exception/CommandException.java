package App.controller.command.exception;

/**
 * Исключение возникающее при выполнении команды
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class CommandException extends Exception{

    public CommandException(String message){
        super(message);
    }

    public CommandException(Exception e){
        super(e);
    }

    public CommandException(String message, Exception e){
        super(message, e);
    }
}
