package App.view;

import App.controller.Controller;
import App.model.entity.Applicant;
import App.view.controls.AddApplicantPane;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Класс для окна редактирования абитуриента
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class EditApplicantWindow {
    private Stage stage;
    private Applicant applicant;

    /**
     * Конструктор, инициализирующий окно редактирования абитуриента
     * @param applicant Абитуриент для редактирования
     */
    public EditApplicantWindow(Applicant applicant) {
        this.applicant = applicant;
        load();
    }

    /**
     * Загрузка графических компонентов из ресурсов
     */
    void load() {
        AddApplicantPane root = new AddApplicantPane(applicant);
        root.setOnClose(EventHandler -> close());
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(MainScreen.class.getResource("style/style.css").toExternalForm());
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit applicant");
        stage.setResizable(false);

        //Получение окна-родителя
        Window window = (Window) Controller
                .getInstance()
                .doReturnCommand("get-current-window");
        Stage InitWindow = window.getStage();
        stage.initModality(Modality.WINDOW_MODAL);
        //Ожидание программой закрытия текущего окна
        stage.showAndWait();
    }

    /**
     * Закрытие окна
     */
    private void close() {
        stage.close();
    }
}
