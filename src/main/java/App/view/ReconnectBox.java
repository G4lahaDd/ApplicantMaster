package App.view;

import App.controller.Controller;
import App.controller.command.Container;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Окно для попытки переподключения
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ReconnectBox implements Initializable {
    private Stage stage;
    private Container<Boolean> result;
    //region Компоненты
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;
    //endregion

    private static byte[] xml;

    /**
     * Конструктор, инициализирующий диалог переподключения
     *
     * @param result Контейнер для передачи значения по ссылке
     */
    private ReconnectBox(Container<Boolean> result) {
        this.result = result;
        load();
    }

    /**
     * Загрузка графических компонентов из ресурсов
     */
    private void load() {
        final FXMLLoader loader = new FXMLLoader();
        try {
            //Если графический компонент загружался, повторная загрузка не требуется
            if (xml == null) {
                xml = getClass().getResource("ReconnectBox.fxml")
                        .openStream()
                        .readAllBytes();
            }
            loader.setController(this);
            Parent root = loader.load(new ByteArrayInputStream(xml));
            Scene scene = new Scene(root, 400, 150);
            scene.getStylesheets().add(MainScreen.class.getResource("style/style.css").toExternalForm());
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Внимание");

            //Получение окна-родителя
            Window window = (Window) Controller
                    .getInstance()
                    .doReturnCommand("get-current-window");
            Stage InitWindow = window.getStage();
            stage.initOwner(InitWindow);
            stage.initModality(Modality.WINDOW_MODAL);
            //Ожидание программой закрытия текущего окна
            stage.showAndWait();
        } catch (IOException ex) {
            System.out.println("Failed to load 'ReconnectBox.fxml' component:\n" + ex.getMessage());
        }
    }

    /**
     * Инициализация графических компонентов диалога
     *
     * @param url            Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnOk.setOnAction(EventHandler -> close(true));
        btnCancel.setOnAction(EventHandler -> close(false));
    }

    /**
     * Закрытие диалога
     *
     * @param result результат закрытия
     */
    private void close(boolean result) {
        this.result.set(result);
        stage.close();
    }

    /**
     * Показывает диалоговое окно для попытки переподключения
     *
     * @return true, если ответ положительный, иначе false
     */
    public static boolean Show() {
        Container<Boolean> result = new Container<>(false);
        new ReconnectBox(result);
        return result.get();
    }
}
