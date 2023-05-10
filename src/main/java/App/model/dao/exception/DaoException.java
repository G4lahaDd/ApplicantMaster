package App.model.dao.exception;

/**
 * Ошибка возникающая при работе с базой данных
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class DaoException extends Exception{
    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }
}
