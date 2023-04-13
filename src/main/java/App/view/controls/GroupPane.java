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

public class GroupPane extends GridPane implements Initializable {
    @FXML
    private Button createButton;
    @FXML
    private Button exportButton;
    @FXML
    private ChoiceBox<String> facultyChoice;
    @FXML
    private ChoiceBox<String> specializationChoice;
    @FXML
    private ListView<StudentTableRow> groupView;
    @FXML
    private VBox groupsList;

    public static final Controller controller = Controller.getInstance();

    private List<FacultyThread> faculties;
    private ObservableList<String> facultiesAbbr;
    private FacultyThread selectedFaculty;
    private SpecializationThread selectedSpecialization;
    private Group selectedGroup;

    public GroupPane() {
        super();
        load();
    }

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        facultyChoice.setValue("Факультет");
        facultyChoice.setDisable(true);
        facultyChoice.setOnAction(EventHandler -> selectFaculty());
        specializationChoice.setValue("Специальность");
        specializationChoice.setDisable(true);
        specializationChoice.setOnAction(EventHandler -> selectSpecialization());

        createButton.setOnAction(EventHandler -> createGroups());
        exportButton.setOnAction(EventHandler -> exportGroups());
        exportButton.setDisable(true);

        facultiesAbbr = FXCollections.observableArrayList();
        facultyChoice.setItems(facultiesAbbr);
    }

    private void createGroups() {
            Param returnParam = new Param();
            controller.doCommand("create-groups", returnParam);
            faculties = (List<FacultyThread>)returnParam.getParameter(ParamName.RETURN);
            facultiesAbbr.clear();
            for (FacultyThread th : faculties) {
                facultiesAbbr.add(th.getAbbreviation());
            }
            facultyChoice.setItems(facultiesAbbr);
            facultyChoice.setDisable(false);
            exportButton.setDisable(false);
    }

    private void exportGroups() {
        Window window = (Window)controller.doReturnCommand("get-current-window");
        Stage thisWindow = window.getStage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Экспорт списков групп");
        File directory = directoryChooser.showDialog(thisWindow);
        if(!directory.exists()) {
            new MessageBox("Директория не выбрана");
            return;
        }
        System.out.println(directory);
        Param params = new Param();
        params.addParameter(ParamName.FILE, directory);
        Controller.getInstance().doCommand("export", params);
    }

    private void selectFaculty() {
        ObservableList<String> specializations = specializationChoice.getItems();
        specializations.clear();
        specializationChoice.setValue("Специальность");

        String abbr = facultyChoice.getValue();
        Optional<FacultyThread> faculty = faculties.stream()
                .filter(x -> x.getAbbreviation().equals(abbr))
                .findFirst();
        if (faculty.isEmpty()) {
            selectedFaculty = null;
            specializationChoice.setDisable(true);
            return;
        }
        specializationChoice.setDisable(false);
        selectedFaculty = faculty.get();
        for (SpecializationThread spec : selectedFaculty.getSpecializations()) {
            specializations.add(spec.getName());
        }
    }

    private void selectSpecialization() {
        groupsList.getChildren().clear();
        selectedGroup = null;
        groupView.getItems().clear();
        String name = specializationChoice.getValue();
        Optional<SpecializationThread> specialization = selectedFaculty
                .getSpecializations().stream()
                .filter(x -> x.getName().equals(name)).findFirst();
        if (specialization.isEmpty()) {
            return;
        }
        selectedSpecialization = specialization.get();
        for (Group group : selectedSpecialization.getGroups()) {

            groupsList.getChildren().add(new GroupToggle(group, this::selectGroup));
        }
    }

    private void selectGroup(Event event) {
        Object object = event.getSource();
        if (!(object instanceof Group)) return;
        Group group = (Group) object;
        ObservableList<StudentTableRow> students = groupView.getItems();
        students.clear();
        if (selectedGroup == group) {
            selectedGroup = null;
            return;
        }
        selectedGroup = group;
        for (Applicant applicant : group.getApplicants()) {
            StudentTableRow row = new StudentTableRow(applicant);
            row.prefWidthProperty().bind(groupView.widthProperty().subtract(25));
            students.add(row);
        }
    }
}
