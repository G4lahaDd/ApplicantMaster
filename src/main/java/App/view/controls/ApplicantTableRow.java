package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.view.EditApplicantWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Класс, описывающий строку таблицы с данными абитуриента
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ApplicantTableRow extends GridPane implements Initializable, Refreshable {
    //region Компоненты
    @FXML
    private Label lblInitials;
    @FXML
    private Label lblBirthday;
    @FXML
    private Label lblFaculty;
    @FXML
    private Label lblLangPoints;
    @FXML
    private Label lblSchoolPoints;
    @FXML
    private Label lblFirstSubjPoints;
    @FXML
    private Label lblSecondSubjPoints;
    @FXML
    private Label lblIsPaid;
    @FXML
    private Button btnEdit;
    //endregion

    private Applicant applicant;

    private static final Controller controller = Controller.getInstance();
    private static byte[] xml;
    private static List<Faculty> faculties;

    /**
     * Конструктор для компонента
     * @param applicant Абитуриент для отображения
     */
    public ApplicantTableRow(Applicant applicant){
        super();
        this.applicant = applicant;
        load();
        //Инициализация необходимых данных
        if(faculties == null){
            faculties = (List<Faculty>)controller.doReturnCommand("load-faculties");
        }
        refresh();
    }

    /**
     * Загрузка компонентов из ресурсов
     */
    private void load(){
        final FXMLLoader loader = new FXMLLoader();
        try {
            //Если ресурс уже загружался, используем его
            if(xml == null) {
                xml = getClass().getResource("ApplicantTableRow.fxml")
                        .openStream()
                        .readAllBytes();
            }
            loader.setController(this);
            Node node = loader.load(new ByteArrayInputStream(xml));
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'ApplicantTableRow.fxml' component:\n" + ex.getMessage());
        }
    }

    /**
     * Обновление данных абитуриента
     */
    @Override
    public void refresh() {
        lblInitials.setText(applicant.getInitials());
        lblBirthday.setText(applicant.getBirthday().toString());
        //Поиск факультета по идентификатору
        Optional<Faculty> optFaculty = faculties.stream().filter(x -> x.getId()
                .equals(applicant.getFacultyId())).findAny();
        if(optFaculty.isEmpty()) return;
        Faculty faculty = optFaculty.get();
        this.lblFaculty.setText(faculty.getAbbreviation());
        lblLangPoints.setText(applicant.getLanguagePoints().toString());
        lblSchoolPoints.setText(applicant.getSchoolMark().toString());
        lblFirstSubjPoints.setText(applicant.getFirstSubjPoints().toString());
        lblSecondSubjPoints.setText(applicant.getSecondSubjPoints().toString());
        lblIsPaid.setText(applicant.getOnPaidBase() ? "П" : "Б");
    }

    /**
     * Инициализация графических компонентов панели
     * @param url Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEdit.setOnAction(EventHandler -> edit());
    }

    /**
     * Вызов окна для изменения данных абитуриента
     */
    private void edit(){
        new EditApplicantWindow(applicant);
        refresh();
    }

    /**
     * Удаление абитуриента
     */
    public void delete(){
        Param params = new Param();
        params.addParameter(ParamName.APPLICANT, applicant);
        Controller.getInstance().doCommand("delete-applicant",params);
    }
}
