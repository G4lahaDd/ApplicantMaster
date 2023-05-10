package App.view;

import javafx.scene.control.Alert;

/**
 * Класс информационного окна для сообщения
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class MessageBox{
    /**
     * Конструктор показывающий информационное окно
     * @param message Сообщение
     */
    public MessageBox(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("style/style.css").toExternalForm());
        alert.showAndWait();
    }
}
