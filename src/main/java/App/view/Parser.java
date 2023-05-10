package App.view;

import App.view.exception.EmptyFieldException;
import javafx.scene.control.TextField;

/**
 * Класс для получения значений из TextField
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class Parser {

    /**
     * Получение текста, если оно имеется
     * @param field Поле ввода для получения значения
     * @return Введённая строка
     * @throws EmptyFieldException Поле ввода пустое
     */
    public static String getText(TextField field) throws EmptyFieldException {
        if (field.getText().isEmpty()) {
            throw new EmptyFieldException();
        }
        return field.getText();
    }

    /**
     * Получение числа, если оно имеется
     * @param field Поле ввода для получения значения
     * @return Введённое число
     * @throws EmptyFieldException Поле ввода пустое
     * @throws NumberFormatException Поле ввода не содержит число
     */
    public static int getInt(TextField field) throws EmptyFieldException, NumberFormatException {
        if (field.getText().isEmpty()) {
            throw new EmptyFieldException();
        }
        int result = Integer.parseInt(field.getText());
        if (result <= 0) throw new NumberFormatException();
        return result;
    }
}
