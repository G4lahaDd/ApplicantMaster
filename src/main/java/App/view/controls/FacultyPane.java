package App.view.controls;

import App.controller.Controller;
import App.controller.command.Container;
import App.controller.command.Delegate;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.view.MessageBox;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class FacultyPane extends GridPane implements Initializable {
    @FXML
    private ScrollPane facultyScroll;
    @FXML
    private ScrollPane specializationsScroll;
    @FXML
    private VBox facultyList;
    @FXML
    private VBox specializations;
    @FXML
    private TextField facultyName;
    @FXML
    private TextField facultyAbbr;
    @FXML
    private TextField facultyCode;
    @FXML
    private Button addFacultyButton;
    @FXML
    private Button updateFacultyButton;
    @FXML
    private Button deleteFacultyButton;
    @FXML
    private Button createSpecializationButton;

    private List<Faculty> faculties;
    private Faculty selectedFaculty;
    private final ToggleGroup facultyGroup = new ToggleGroup();
    private final Delegate selectFacultyMethod = param -> selectFaculty((Faculty) param);

    public FacultyPane() {
        super();
        load();
    }

    public FacultyPane(List<Faculty> faculties) {
        super();
        load();
        this.faculties = faculties;
        init();
    }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addFacultyButton.setOnAction(EventHandler -> addNewFaculty());
        updateFacultyButton.setOnAction(EventHandler -> updateFaculty());
        createSpecializationButton.setOnAction(EventHandler -> createSpecialization());
        deleteFacultyButton.setOnAction(EventHandler -> deleteFaculty());
        facultyList.prefWidthProperty().bind(facultyScroll.widthProperty());
        facultyList.setPadding(new Insets(0,18,0,0));
        specializations.prefWidthProperty().bind(specializationsScroll.widthProperty());
        specializations.setPadding(new Insets(0,18,0,0));
    }

    private void init() {
        facultyList.getChildren().clear();
        SpecializationPane.setOnDelete(this::deleteSpecialization);
        for (Faculty faculty : faculties) {
            facultyList.getChildren().add(new FacultyToggle(
                    faculty, selectFacultyMethod, facultyGroup));
        }
    }

    @FXML
    private void addNewFaculty() {
        Faculty faculty = new Faculty();
        faculties.add(faculty);
        FacultyToggle facultyToggle = new FacultyToggle(faculty, selectFacultyMethod, facultyGroup);
        facultyList.getChildren().add(facultyToggle);
        facultyToggle.setSelected(true);
        selectedFaculty = faculty;
        updateFacultyData();
    }
    @FXML
    private void updateFaculty() {
        String name = facultyName.getText();
        String abbr = facultyAbbr.getText();
        int code;
        try {
            code = Integer.parseInt(facultyCode.getText());
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
        Param param = new Param();
        param.addParameter(ParamName.FACULTY, selectedFaculty);
        Controller.getInstance().doCommand("", param);
    }

    private void deleteFaculty(){
        if(selectedFaculty == null) return;
        Param param = new Param();
        param.addParameter(ParamName.FACULTY, selectedFaculty);
        Controller.getInstance().doCommand("delete-faculty", param);
        FacultyToggle toggle = (FacultyToggle) facultyList.getChildren().stream()
                .filter(x -> ((FacultyToggle)x).getFaculty().equals(selectedFaculty))
                .findFirst().get();
        facultyList.getChildren().remove(toggle);
        selectedFaculty = null;
        updateFacultyData();
    }
    private void updateFacultyData() {
        specializations.getChildren().clear();
        if (selectedFaculty == null) {
            facultyName.setText("");
            facultyAbbr.setText("");
            facultyCode.setText("");
        } else {
            facultyName.setText(selectedFaculty.getName());
            facultyAbbr.setText(selectedFaculty.getAbbreviation());
            facultyCode.setText(selectedFaculty.getGroupCode().toString());
            if (selectedFaculty.getSpecializations() != null)
                for (Specialization spec : selectedFaculty.getSpecializations()) {
                    specializations.getChildren().add(new SpecializationPane(spec));
                }
        }
    }

    private void createSpecialization(){
        if(selectedFaculty == null) {
            new MessageBox("Не выбран факультет");
            return;
        }
        Container<Specialization> spec = new Container<>();
        new EditSpecializationWindow(spec);
        if(spec.isEmpty()) return;
        selectedFaculty.addSpecializations(spec.get());
        specializations.getChildren().add(new SpecializationPane(spec.get()));
    }

    private void deleteSpecialization(Object param){

        if(!(param instanceof SpecializationPane)) return;
        SpecializationPane specPane = (SpecializationPane)param;
        selectedFaculty.removeSpecializations(specPane.getSpecialization());
        specializations.getChildren().remove(specPane);
    }

    private void selectFaculty(Faculty faculty) {
        if (selectedFaculty != null && selectedFaculty.equals(faculty)) {
            selectedFaculty = null;
        } else {
            selectedFaculty = faculty;
        }
        System.out.println("Selected faculty : " + faculty.getAbbreviation());
        updateFacultyData();
    }

    private boolean validateFaculty(String name, String abbr, int code){
        if(name.isEmpty() && abbr.isEmpty()
                && code < 1){
            return false;
        }
        return true;
    }

    public Faculty getSelectedFaculty() {
        return selectedFaculty;
    }


}
