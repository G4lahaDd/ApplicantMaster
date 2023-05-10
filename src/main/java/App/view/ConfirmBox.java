package App.view;

import App.controller.Controller;
import App.controller.command.Container;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Окно для подтверждения действия
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ConfirmBox implements Initializable {
    private Stage stage;
    private Container<Boolean> result;
    private String message;

    //region Компоненты
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;
    //endregion

    private static byte[] xml;

    /**
     * Конструктор, инициализирующий окно для подтверждания
     * @param message Сообщение
     * @param result контейнер для записи результата
     */
    private ConfirmBox(String message, Container<Boolean> result) {
        this.result = result;
        this.message = message;
        load();
    }

    /**
     * Загрузка графических компонентов из ресурсов
     */
    private void load(){
        final FXMLLoader loader = new FXMLLoader();
        try {
            //Если компонент уже загружался, повторная загрузка не требуется
            if (xml == null) {
                xml = getClass().getResource("ConfirmBox.fxml")
                        .openStream()
                        .readAllBytes();
            }
            loader.setController(this);
            Parent root = loader.load(new ByteArrayInputStream(xml));
            Scene scene = new Scene(root, 400, 150);
            scene.getStylesheets().add(MainScreen.class.getResource("style/style.css").toExternalForm());
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Подтверждение");

            //Получение окна-родителя
            Window window = (Window) Controller
                    .getInstance()
                    .doReturnCommand("get-current-window");
            Stage InitWindow = window.getStage();
            stage.initOwner(InitWindow);
            stage.initModality(Modality.WINDOW_MODAL);
            //Ожидание программы закрытия данного окна
            stage.showAndWait();
        }  catch (IOException ex) {
            System.out.println("Failed to load 'ConfirmBox.fxml' component:\n" + ex.getMessage());
        }
    }

    /**
     * Инициализация графических компонентов панели
     *
     * @param url            Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblMessage.setText(message);
        btnOk.setOnAction(EventHandler -> close(true));
        btnCancel.setOnAction(EventHandler -> close(false));
    }

    /**
     * Закрытие окна
     * @param result результат при закрытии
     */
    private void close(boolean result) {
        this.result.set(result);
        stage.close();
    }

    /**
     * Открытие окна для подтверждения
     * @param message Сообщение
     * @return результат подтверждения
     */
    public static boolean Show(String message) {
        //Создание контейнера для передачи данных по ссылке
        Container<Boolean> result = new Container<>(false);
        //Создание окна
        new ConfirmBox(message, result);
        return result.get();
    }
}
