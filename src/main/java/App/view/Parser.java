package App.view;

import App.view.Exception.EmptyFieldException;
import javafx.scene.control.TextField;

public class Parser {
    public static String getText(TextField field) throws EmptyFieldException {
        if (field.getText().isEmpty()) {
            throw new EmptyFieldException();
        }
        return field.getText();
    }

    public static int getInt(TextField field) throws EmptyFieldException, NumberFormatException {
        if (field.getText().isEmpty()) {
            throw new EmptyFieldException();
        }
        int result = Integer.parseInt(field.getText());
        if (result <= 0) throw new NumberFormatException();
        return result;
    }
}
