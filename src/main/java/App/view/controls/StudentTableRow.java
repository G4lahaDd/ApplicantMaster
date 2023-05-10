package App.view.controls;

import App.model.entity.Applicant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Класс, описывающий строку для студента в таблице
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class StudentTableRow extends GridPane implements Initializable, Refreshable {
    //region Компоненты
    @FXML
    private Label lblInitials;
    @FXML
    private Label lblBirthday;
    @FXML
    private Label lblPoints;
    @FXML
    private Label lblIsPaid;
    //endregion

    private Applicant applicant;
    private static byte[] xml;

    /**
     * Конструктор, инициализирующий строку в таблице для студента
     * @param applicant струдент
     */
    public StudentTableRow(Applicant applicant){
        super();
        this.applicant = applicant;
        load();
        refresh();
    }

    /**
     * Загрузка графических компонентов из ресурсов
     */
    private void load(){
        final FXMLLoader loader = new FXMLLoader();
        try {
            //Если компонент уже загружался из ресурсов, повторная загрузка не требуется
            if(xml == null) {
                xml = getClass().getResource("StudentTableRow.fxml")
                        .openStream()
                        .readAllBytes();
            }
            loader.setController(this);
            Node node = loader.load(new ByteArrayInputStream(xml));
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'StudentTableRow.fxml' component:\n" + ex.getMessage());
        }
    }

    /**
     * Обновление данных строки
     */
    @Override
    public void refresh() {
        lblInitials.setText(applicant.getInitials());
        lblBirthday.setText(applicant.getBirthday().toString());
        lblPoints.setText(Integer.toString(applicant.getTotalMark()));
        lblIsPaid.setText(applicant.getOnPaidBase() ? "П" : "Б");
    }

    /**
     * Инициализация графических компонентов панели
     * @param url Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
