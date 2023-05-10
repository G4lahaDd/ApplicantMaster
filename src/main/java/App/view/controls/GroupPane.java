package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Applicant;
import App.model.entity.groups.FacultyThread;
import App.model.entity.groups.Group;
import App.model.entity.groups.SpecializationThread;
import App.view.MessageBox;
import App.view.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Класс, описывающий панель для отображения групп
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class GroupPane extends GridPane implements Initializable {
    //region Компоненты
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnExport;
    @FXML
    private ChoiceBox<String> chbFaculty;
    @FXML
    private ChoiceBox<String> chbSpecialization;
    @FXML
    private ListView<StudentTableRow> groupView;
    @FXML
    private VBox boxGroupsList;
    //endregion

    public static final Controller controller = Controller.getInstance();

    private List<FacultyThread> faculties;
    private ObservableList<String> facultiesAbbr;
    private FacultyThread selectedFaculty;
    private SpecializationThread selectedSpecialization;
    private Group selectedGroup;

    /**
     * Конструктор инициализирующий панель для групп
     */
    public GroupPane() {
        super();
        load();
    }

    /**
     * Загрузка графических компонентов из ресурсов
     */
    private void load() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GroupsPane.fxml"));
            loader.setController(this);
            Node node = loader.load();
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'GroupsPane.fxml' component:\n" + ex.getMessage());
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
        chbFaculty.setValue("Факультет");
        chbFaculty.setDisable(true);
        chbFaculty.setOnAction(EventHandler -> selectFaculty());
        chbSpecialization.setValue("Специальность");
        chbSpecialization.setDisable(true);
        chbSpecialization.setOnAction(EventHandler -> selectSpecialization());

        btnCreate.setOnAction(EventHandler -> createGroups());
        btnExport.setOnAction(EventHandler -> exportGroups());
        btnExport.setDisable(true);

        facultiesAbbr = FXCollections.observableArrayList();
        chbFaculty.setItems(facultiesAbbr);
    }

    /**
     * Создание групп
     */
    private void createGroups() {
        Param returnParam = new Param();
        controller.doCommand("create-groups", returnParam);
        //Получение потоков студентов
        faculties = (List<FacultyThread>) returnParam.getParameter(ParamName.RETURN);
        facultiesAbbr.clear();
        for (FacultyThread th : faculties) {
            facultiesAbbr.add(th.getAbbreviation());
        }
        chbFaculty.setItems(facultiesAbbr);
        chbFaculty.setDisable(false);
        btnExport.setDisable(false);
    }

    /**
     * Экпорт групп в Excel формат
     */
    private void exportGroups() {
        //Получение текущего окна
        Window window = (Window) controller.doReturnCommand("get-current-window");
        Stage thisWindow = window.getStage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Экспорт списков групп");
        //Выбор директории для сохранения
        File directory = directoryChooser.showDialog(thisWindow);
        if (directory == null) return;
        if (!directory.exists()) {
            new MessageBox("Директория не выбрана");
            return;
        }
        System.out.println(directory);
        //Вызов экспорта в Excel
        Param params = new Param();
        params.addParameter(ParamName.FILE, directory);
        Controller.getInstance().doCommand("export", params);
    }

    /**
     * Выбор факультета
     */
    private void selectFaculty() {
        ObservableList<String> specializations = chbSpecialization.getItems();
        specializations.clear();
        chbSpecialization.setValue("Специальность");

        //Поиск выбранного факультета
        String abbr = chbFaculty.getValue();
        Optional<FacultyThread> faculty = faculties.stream()
                .filter(x -> x.getAbbreviation().equals(abbr))
                .findFirst();
        if (faculty.isEmpty()) {
            selectedFaculty = null;
            chbSpecialization.setDisable(true);
            return;
        }
        chbSpecialization.setDisable(false);
        selectedFaculty = faculty.get();
        //Обновление списка специальностей
        for (SpecializationThread spec : selectedFaculty.getSpecializations()) {
            specializations.add(spec.getName());
        }
    }

    /**
     * Выбор специальности
     */
    private void selectSpecialization() {
        boxGroupsList.getChildren().clear();
        selectedGroup = null;
        groupView.getItems().clear();
        //Получение выбранной специальности
        String name = chbSpecialization.getValue();
        Optional<SpecializationThread> specialization = selectedFaculty
                .getSpecializations().stream()
                .filter(x -> x.getName().equals(name)).findFirst();
        if (specialization.isEmpty()) {
            return;
        }
        //Обновление списка групп
        selectedSpecialization = specialization.get();
        for (Group group : selectedSpecialization.getGroups()) {
            boxGroupsList.getChildren().add(new GroupToggle(group, this::selectGroup));
        }
    }

    /**
     * Выбор группы
     * @param event Объект события, содержащий группу
     */
    private void selectGroup(Event event) {
        Object object = event.getSource();
        if (!(object instanceof Group)) return;
        Group group = (Group) object;
        //Получение списка студентов
        ObservableList<StudentTableRow> students = groupView.getItems();
        students.clear();
        if (selectedGroup == group) {
            selectedGroup = null;
            return;
        }
        selectedGroup = group;
        //Отображение списка студентов
        for (Applicant applicant : group.getApplicants()) {
            StudentTableRow row = new StudentTableRow(applicant);
            row.prefWidthProperty().bind(groupView.widthProperty().subtract(25));
            students.add(row);
        }
    }
}
