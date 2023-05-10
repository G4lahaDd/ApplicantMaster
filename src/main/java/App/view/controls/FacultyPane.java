package App.view.controls;

import App.controller.Controller;
import App.controller.command.Container;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.view.ConfirmBox;
import App.view.MessageBox;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Класс, описывающий панель для отображения факультетов
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class FacultyPane extends GridPane implements Initializable {
    //region Компоненты
    @FXML
    private ScrollPane scrFaculty;
    @FXML
    private ScrollPane scrSpecializations;
    @FXML
    private VBox boxFacultyList;
    @FXML
    private VBox boxSpecializations;
    @FXML
    private TextField tfFacultyName;
    @FXML
    private TextField tfFacultyAbbr;
    @FXML
    private TextField tfFacultyCode;
    @FXML
    private Button btnAddFaculty;
    @FXML
    private Button btnUpdateFaculty;
    @FXML
    private Button btnDeleteFaculty;
    @FXML
    private Button btnCreateSpecialization;
    @FXML
    private Label lblWarning;
    //endregion
    private List<Faculty> faculties;
    private List<Faculty> nonSavedFaculties;

    private Faculty selectedFaculty;
    private final ToggleGroup facultyGroup = new ToggleGroup();
    private final EventHandler selectFacultyMethod = e -> selectFaculty(e);

    /**
     * Конструктор с инифиализацией графических компонентов
     */
    public FacultyPane() {
        super();
        load();
    }

    /**
     * Конструктор с инифиализацией графических компонентов,
     * заполняющий список факультетов
     * @param faculties список факультетов
     */
    public FacultyPane(List<Faculty> faculties) {
        super();
        load();
        this.faculties = faculties;
        initFaculties();
        nonSavedFaculties = new ArrayList<>();
    }

    /**
     * Загрузка графических элементов окна
     */
    private void load() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("facultyPane.fxml"));
        try {
            loader.setController(this);
            Node node = loader.load();
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'facultyPane.fxml' component:\n" + ex.getMessage());
        }
    }

    /**
     * Инициализация графических компонентов панели
     * @param url Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblWarning.setVisible(false);

        btnAddFaculty.setOnAction(EventHandler -> addNewFaculty());
        btnUpdateFaculty.setOnAction(EventHandler -> updateFaculty());
        btnCreateSpecialization.setOnAction(EventHandler -> createSpecialization());
        btnDeleteFaculty.setOnAction(EventHandler -> deleteFaculty());
        boxFacultyList.prefWidthProperty().bind(scrFaculty.widthProperty());
        boxFacultyList.setPadding(new Insets(0,18,0,0));
        boxSpecializations.prefWidthProperty().bind(scrSpecializations.widthProperty());
        boxSpecializations.setPadding(new Insets(0,18,0,0));
    }

    /**
     * Инициализация списка факультетов
     */
    private void initFaculties() {
        boxFacultyList.getChildren().clear();
        SpecializationPane.setOnDelete(this::deleteSpecialization);
        for (Faculty faculty : faculties) {
            boxFacultyList.getChildren().add(new FacultyToggle(
                    faculty, selectFacultyMethod, facultyGroup));
        }
    }

    /**
     * Добавление нового факультета
     */
    @FXML
    private void addNewFaculty() {
        Faculty faculty = new Faculty();
        faculties.add(faculty);
        FacultyToggle facultyToggle = new FacultyToggle(faculty, selectFacultyMethod, facultyGroup);
        boxFacultyList.getChildren().add(facultyToggle);
        facultyToggle.setSelected(true);
        selectedFaculty = faculty;
        updateFacultyData();
        //Отображение несохранённых данных
        nonSavedFaculties.add(faculty);
        lblWarning.setVisible(true);
    }

    /**
     * Обновление данных факультета
     */
    @FXML
    private void updateFaculty() {
        //Заполнение полей
        String name = tfFacultyName.getText();
        String abbr = tfFacultyAbbr.getText();
        int code;
        try {
            code = Integer.parseInt(tfFacultyCode.getText());
        }
        catch (Exception ex){
            new MessageBox("Ошибка ввода номера факультета");
            return;
        }
        if(!validateFaculty(name, abbr, code)){
            new MessageBox("Ошибка введённых данных");
            return;
        }
        selectedFaculty.setName(name);
        selectedFaculty.setAbbreviation(abbr);
        selectedFaculty.setGroupCode(code);
        //Обновление в бд
        Param param = new Param();
        param.addParameter(ParamName.FACULTY, selectedFaculty);
        String command = "add-faculty";
        if(selectedFaculty.getId() != null && selectedFaculty.getId() != -1) command = "update-faculty";
        Controller.getInstance().doCommand(command, param);
        //Проверка результатов
        Boolean result = (Boolean) param.getParameter(ParamName.RETURN);
        if(result){
            nonSavedFaculties.remove(selectedFaculty);
            lblWarning.setVisible(false);
        }
        else{
            new MessageBox("Не удалось добавить факультет");
        }
    }

    /**
     * Удаление факультета
     */
    private void deleteFaculty(){
        if(selectedFaculty == null) return;

        String message = "Вы уверены что хотите удалить "
                + selectedFaculty.getAbbreviation() + "?";
        if(!ConfirmBox.Show(message)){
            return;
        }

        Param param = new Param();
        param.addParameter(ParamName.FACULTY, selectedFaculty);
        Controller.getInstance().doCommand("delete-faculty", param);
        FacultyToggle toggle = (FacultyToggle) boxFacultyList.getChildren().stream()
                .filter(x -> ((FacultyToggle)x).getFaculty().equals(selectedFaculty))
                .findFirst().get();
        boxFacultyList.getChildren().remove(toggle);
        faculties.remove(selectedFaculty);
        selectedFaculty = null;
        nonSavedFaculties.remove(selectedFaculty);
        lblWarning.setVisible(false);
        updateFacultyData();
    }

    /**
     * Обновление данных факультета на экране
     */
    private void updateFacultyData() {
        boxSpecializations.getChildren().clear();
        if (selectedFaculty == null) {
            tfFacultyName.setText("");
            tfFacultyAbbr.setText("");
            tfFacultyCode.setText("");
            lblWarning.setVisible(false);
        } else {
            tfFacultyName.setText(selectedFaculty.getName());
            tfFacultyAbbr.setText(selectedFaculty.getAbbreviation());
            tfFacultyCode.setText(selectedFaculty.getGroupCode().toString());
            if (selectedFaculty.getSpecializations() != null)
                for (Specialization spec : selectedFaculty.getSpecializations()) {
                    boxSpecializations.getChildren().add(new SpecializationPane(spec, e -> updateSpecialization(spec)));
                }
        }
    }

    /**
     * Создание специальности у факультета
     */
    private void createSpecialization(){
        if(selectedFaculty == null) {
            new MessageBox("Не выбран факультет");
            return;
        }
        Container<Specialization> spec = new Container<>();
        new EditSpecializationWindow(spec);
        if(spec.isEmpty()) return;
        selectedFaculty.addSpecializations(spec.get());
        boxSpecializations.getChildren().add(new SpecializationPane(spec.get(), e -> updateSpecialization(spec.get())));
        if(!nonSavedFaculties.contains(selectedFaculty))
            nonSavedFaculties.add(selectedFaculty);
        lblWarning.setVisible(true);
    }

    /**
     * Обновление специальности
     * @param specialization Специальность
     */
    private void updateSpecialization(Specialization specialization){
        if(!nonSavedFaculties.contains(selectedFaculty))
            nonSavedFaculties.add(selectedFaculty);
        lblWarning.setVisible(true);
    }

    /**
     * Удаление специальности
     * @param event Объект события, содержащий специальность
     */
    private void deleteSpecialization(Event event){
        Object object = event.getSource();
        if(!(object instanceof SpecializationPane)) return;
        SpecializationPane specPane = (SpecializationPane)object;
        selectedFaculty.removeSpecializations(specPane.getSpecialization());
        boxSpecializations.getChildren().remove(specPane);
        if(!nonSavedFaculties.contains(selectedFaculty))
            nonSavedFaculties.add(selectedFaculty);
        lblWarning.setVisible(true);
    }

    /**
     * Выбор факультета
     * @param event Объект события, содержащий факультет
     */
    private void selectFaculty(Event event) {
        Faculty faculty = (Faculty)event.getSource();
        if (selectedFaculty != null && selectedFaculty.equals(faculty)) {
            selectedFaculty = null;
            lblWarning.setVisible(false);
        } else {
            selectedFaculty = faculty;
            lblWarning.setVisible(nonSavedFaculties.contains(selectedFaculty));
        }
        System.out.println("Selected faculty : " + faculty.getAbbreviation());
        updateFacultyData();
    }

    /**
     * Проверка факультета на правильность введённых данных
     * @param name название факультета
     * @param abbr Аббревиатура факультета
     * @param code Код Факультета
     * @return true - если данные правильны, иначе false
     */
    private boolean validateFaculty(String name, String abbr, int code){
        if(name.isEmpty() && abbr.isEmpty()
                && (code < 1 || code > 100)){
            return false;
        }
        return true;
    }

    /**
     * Получение выбранного факультета
     * @return выбранный факультет
     */
    public Faculty getSelectedFaculty() {
        return selectedFaculty;
    }


}
