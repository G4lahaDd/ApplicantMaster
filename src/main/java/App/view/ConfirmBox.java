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

public class ConfirmBox implements Initializable {
    private Stage stage;
    private Container<Boolean> result;
    private String message;

    @FXML
    private Label messageLabel;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;


    private static byte[] xml;

    private ConfirmBox(String message, Container<Boolean> result) {
        this.result = result;
        this.message = message;
        load();
    }

    private void load(){
        final FXMLLoader loader = new FXMLLoader();
        try {
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

            Window window = (Window) Controller
                    .getInstance()
                    .doReturnCommand("get-current-window");
            Stage InitWindow = window.getStage();
            stage.initOwner(InitWindow);
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        }  catch (IOException ex) {
            System.out.println("Failed to load 'ConfirmBox.fxml' component:\n" + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageLabel.setText(message);
        ok.setOnAction(EventHandler -> close(true));
        cancel.setOnAction(EventHandler -> close(false));
    }

    private void close(boolean result) {
        this.result.set(result);
        stage.close();
    }

    public static boolean Show(String message) {
        Container<Boolean> result = new Container<>(false);
        new ConfirmBox(message, result);
        return result.get();
    }
}
