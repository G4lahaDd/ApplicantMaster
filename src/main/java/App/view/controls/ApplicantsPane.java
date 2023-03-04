package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.service.ApplicationDataService;
import App.view.MessageBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ApplicantsPane extends GridPane implements Initializable, Refreshable {
    @FXML
    private Button addButton;
    @FXML
    private Button findButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox applicantsList;
    @FXML
    private ChoiceBox<String> facultyChoiceBox;
    @FXML
    private ChoiceBox<String> specChoiceBox;
    @FXML
    private TextField queryField;
    @FXML
    private CheckBox isPaidCheck;

    private ObservableList<String> facultiesAbbr;
    private ObservableList<String> specCodes;
    private List<Faculty> faculties;
    private Faculty selectedFaculty;
    private Specialization selectedSpecialization;

    public ApplicantsPane() {
        super();
        load();
    }

    private void load() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicantsPane.fxml"));
        try {
            loader.setController(this);
            Node node = loader.load();
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'ApplicantsPane.fxml' component:\n" + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        faculties = ApplicationDataService.getInstance().getFaculties();
        applicantsList.prefWidthProperty().bind(scrollPane.widthProperty());
        applicantsList.setPadding(new Insets(0, 20, 0, 0));

        facultiesAbbr = FXCollections.observableArrayList();
        if (faculties != null)
            for (Faculty faculty : faculties) {
                facultiesAbbr.add(faculty.getAbbreviation());
            }
        facultiesAbbr.add("Любой");
        facultyChoiceBox.setOnAction(EventHandler -> selectFaculty());
        facultyChoiceBox.setItems(facultiesAbbr);
        facultyChoiceBox.setValue("Любой");

        updateSpecChoiceBox();
        specChoiceBox.setOnAction(EventHandler -> selectSpecialization());

        findButton.setOnAction(EventHandler -> find());
    }

    @Override
    public void refresh() {
        facultiesAbbr = FXCollections.observableArrayList();
        if (faculties != null)
            for (Faculty faculty : faculties) {
                facultiesAbbr.add(faculty.getAbbreviation());
            }
        facultiesAbbr.add("Любой");
        facultyChoiceBox.setItems(facultiesAbbr);
        facultyChoiceBox.setValue("Любой");

        updateSpecChoiceBox();
    }

    private void selectFaculty() {
        String abbr = facultyChoiceBox.getValue();
        if (abbr == null) return;
        if (abbr.isEmpty() || abbr.equals("Любой")) {
            selectedFaculty = null;
            updateSpecChoiceBox();
        } else {
            Optional<Faculty> faculty = faculties.stream()
                    .filter(x -> x.getAbbreviation()
                    .equals(abbr)).findFirst();
            if(faculty.isEmpty()) return;
            selectedFaculty = faculty.get();

            specCodes = FXCollections.observableArrayList();
            specCodes.add("Любая");
            for (Specialization spec : selectedFaculty.getSpecializations()) {
                specCodes.add(spec.getCode());
            }
            specChoiceBox.setItems(specCodes);
            specChoiceBox.setValue("Любая");
        }
    }

    private void updateSpecChoiceBox() {
        specCodes = FXCollections.observableArrayList();
        specCodes.add("Любая");
        specChoiceBox.setItems(specCodes);
        specChoiceBox.setValue("Любая");
        selectedSpecialization = null;
    }

    private void selectSpecialization() {
        String code = specChoiceBox.getValue();
        if(code == null) return;
        if(code.isEmpty() || code.equals("Любая")){
            selectedSpecialization = null;
        }else{
            if(selectedFaculty == null) return;
            Optional<Specialization> specialization = selectedFaculty
                    .getSpecializations().stream()
                    .filter(x -> x.getCode().equals(code))
                    .findFirst();
            if(specialization.isEmpty()) return;
            selectedSpecialization = specialization.get();
        }
    }

    private void find(){
        Param param = new Param();
        param.addParameter(ParamName.RESPONSE, (EventHandler)this::getResult);

        if(selectedFaculty != null)
            param.addParameter(ParamName.FACULTY, selectedFaculty.getId());
        if(selectedSpecialization != null)
            param.addParameter(ParamName.SPECIALIZATION, selectedSpecialization.getId());

        String initials = queryField.getText();
        if(!initials.isEmpty()){
            String[] query = initials.trim().split(" ");
            if(query.length >= 1)
                param.addParameter(ParamName.SURNAME, query[0]);
            if(query.length >= 2)
                param.addParameter(ParamName.NAME, query[1]);
            if(query.length >= 3)
                param.addParameter(ParamName.PATRONYMIC, query[2]);
        }

        if(isPaidCheck.isSelected()){
            param.addParameter(ParamName.IS_PAID, true);
        }

        Controller.getInstance().doCommand("find-applicants", param);
    }

    private void getResult(Event event){
        Object object = event.getSource();
        if(object == null){
            new MessageBox("Абитуриенты не найдены");
            return;
        }
        if(object instanceof List){
            if(((List<?>) object).isEmpty()){
                new MessageBox("Абитуриенты не найдены");
                return;
            }
            List<Applicant> applicants = (List<Applicant>)object;
            applicantsList.getChildren().clear();
            for (int i = 0; i < applicants.size(); i++){
                applicantsList.getChildren().add(
                        new ApplicantTableRow(applicants.get(i)));
            }
        }
    }
}
