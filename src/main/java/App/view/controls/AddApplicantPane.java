package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.entity.Subject;
import App.model.service.ApplicationDataService;
import App.view.Exception.EmptyFieldException;
import App.view.Main;
import App.view.MainScreen;
import App.view.MessageBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AddApplicantPane extends GridPane implements Initializable, Refreshable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField patronymicField;
    @FXML
    private TextField langPoints;
    @FXML
    private TextField schoolPoints;
    @FXML
    private TextField firstPoints;
    @FXML
    private TextField secondPoints;
    @FXML
    private Label firstSubject;
    @FXML
    private Label secondSubject;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ScrollPane specializationScroll;
    @FXML
    private VBox specializationList;
    @FXML
    private ChoiceBox<String> facultyChoiceBox;
    @FXML
    private CheckBox isPaidCheckBox;
    @FXML
    private GridPane pointsPane;
    @FXML
    private Button addButton;

    private List<Faculty> faculties;
    private Faculty selectedFaculty;
    private Applicant applicant;
    private Subject selectedFirstSubj;
    private Subject selectedSecondSubj;
    private PaneMode paneMode;
    private EventHandler onClose;

    public AddApplicantPane() {
        super();
        load();
        loadData();
        paneMode = PaneMode.ADD;
    }

    public AddApplicantPane(Applicant applicant) {
        super();
        load();
        this.applicant = applicant;
        loadData();
        paneMode = PaneMode.EDIT;
        updateDataOnWindow();
    }

    private void loadData() {
        faculties = ApplicationDataService.getInstance().getFaculties();
        ObservableList<String> facultiesAbbr = FXCollections.observableArrayList();
        for (Faculty faculty : faculties) {
            facultiesAbbr.add(faculty.getAbbreviation());
        }
        facultyChoiceBox.setItems(facultiesAbbr);
        facultyChoiceBox.setOnAction(EventHandler -> selectFaculty());
    }

    private void load() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("AddApplicantPane.fxml"));
        try {
            loader.setController(this);
            Node node = loader.load();
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'AddApplicantPane.fxml' component:\n" + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pointsPane.setVisible(false);
        addButton.setOnAction(EventHandler -> add());
        specializationList.prefWidthProperty().bind(specializationScroll.widthProperty());
        specializationList.setPadding(new Insets(0, 18, 0, 0));
        SpecializationToggle.setOnChooseFirst(this::selectFirstSpecialization);
        SpecializationToggle.setOnLostFirst(EventHandler -> onLostSpecialization());

        //Перегрузка конвертера с обработкой исключения, стандартный имеет необрабатываемое исключение
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            private final StringConverter<LocalDate> base = datePicker.getConverter();

            @Override
            public String toString(LocalDate date) {
                return base.toString(date);
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return base.fromString(string);
                } catch (DateTimeParseException ex) {
                    System.out.println("Неверно введена дата");
                    return null;
                }
            }
        };
        datePicker.setConverter(converter);
    }

    @Override
    public void refresh() {
        ObservableList<String> facultiesAbbr = facultyChoiceBox.getItems();
        facultiesAbbr.clear();
        for (Faculty faculty : faculties) {
            facultiesAbbr.add(faculty.getAbbreviation());
        }
    }

    public void setOnClose(EventHandler onClose) {
        this.onClose = onClose;
    }

    private void selectFaculty() {
        pointsPane.setVisible(false);
        String selAbbr = facultyChoiceBox.getValue();
        if (selAbbr == null) return;
        if (selAbbr.isEmpty()) return;
        selectedFaculty = faculties.stream().filter(x -> x.getAbbreviation().equals(selAbbr))
                .findAny().get();
        specializationList.getChildren().clear();
        SpecializationToggle.clear();
        if (selectedFaculty.getSpecializations() == null) return;
        for (Specialization specialization : selectedFaculty.getSpecializations()) {
            specializationList.getChildren().add(new SpecializationToggle(specialization));
        }
    }

    private void selectFirstSpecialization(Event event) {
        Object source = event.getSource();
        if (!(source instanceof SpecializationToggle)) return;
        SpecializationToggle toggle = (SpecializationToggle) source;
        Specialization specialization = toggle.getSpecialization();
        selectedFirstSubj = specialization.getFirstSubject();
        selectedSecondSubj = specialization.getSecondSubject();

        for (int i = specializationList.getChildren().size() - 1; i >= 0; i--) {
            SpecializationToggle specToggle = (SpecializationToggle) specializationList.getChildren().get(i);
            if (specToggle.getSpecialization().getFirstSubject() != selectedFirstSubj
                    || specToggle.getSpecialization().getSecondSubject() != selectedSecondSubj) {
                specializationList.getChildren().remove(specToggle);
            }
        }

        firstSubject.setText(selectedFirstSubj.toString());
        secondSubject.setText(selectedSecondSubj.toString());
        pointsPane.setVisible(true);
    }

    private void onLostSpecialization() {
        pointsPane.setVisible(false);
        specializationList.getChildren().clear();
        for (Specialization specialization : selectedFaculty.getSpecializations()) {
            specializationList.getChildren().add(new SpecializationToggle(specialization));
        }
    }

    private void updateDataOnWindow() {
        if (applicant == null) return;
        nameField.setText(applicant.getName());
        surnameField.setText(applicant.getSurname());
        patronymicField.setText(applicant.getPatronymic());
        datePicker.setValue(applicant.getBirthday());
        isPaidCheckBox.setSelected(applicant.getOnPaidBase());

        langPoints.setText(applicant.getLanguagePoints().toString());
        schoolPoints.setText(applicant.getSchoolMark().toString());
        firstPoints.setText(applicant.getFirstSubjPoints().toString());
        secondPoints.setText(applicant.getSecondSubjPoints().toString());

        int facultyId = applicant.getFacultyId();
        Optional<Faculty> faculty = faculties.stream().filter(x -> x.getId().equals(facultyId)).findFirst();
        if (faculty.isEmpty()) return;
        selectedFaculty = faculty.get();
        facultyChoiceBox.setValue(faculty.get().getAbbreviation());
        Map<Integer, Integer> prioritySpec = applicant.getPrioritySpecializations();
        for(int i = 1; i <= prioritySpec.size(); i++){
            int specId = prioritySpec.get(i);
            Optional<Node> toggle = specializationList
                    .getChildren().stream()
                    .filter(x -> ((SpecializationToggle)x).getSpecialization()
                    .getId().equals(specId)).findFirst();
            if(toggle.isEmpty()) continue;
            ((SpecializationToggle)toggle.get()).select();
        }
    }

    private void add() {
        try {
            String surname = getText(surnameField);
            String name = getText(nameField);
            String patronymic = getText(nameField);
            int lang = getInt(langPoints);
            int firstSubj = getInt(firstPoints);
            int secondSubj = getInt(secondPoints);
            int schoolPoints = getInt(this.schoolPoints);

            LocalDate birthday = datePicker.getValue();
            if (birthday == null) throw new DateTimeException("null");

            boolean isPaid = isPaidCheckBox.isSelected();

            Map<Integer, Integer> specializationMap = new HashMap<>();
            for (Node node : specializationList.getChildren()) {
                SpecializationToggle toggle = (SpecializationToggle) node;
                if (toggle.getIndex() > 0) {
                    specializationMap.put(toggle.getIndex(), toggle.getSpecialization().getId());
                }
            }
            if (specializationMap.size() == 0) {
                new MessageBox("необходимо выбрать специальности");
                return;
            }

            String commandName = "edit-applicant";
            if (paneMode == PaneMode.ADD) {
                applicant = new Applicant();
                commandName = "add-applicant";
            }
            applicant.setName(name);
            applicant.setSurname(surname);
            applicant.setPatronymic(patronymic);
            applicant.setFacultyId(selectedFaculty.getId());
            applicant.setPrioritySpecializations(specializationMap);
            applicant.setSchoolMark(schoolPoints);
            applicant.setLanguagePoints(lang);
            applicant.setFirstSubjPoints(firstSubj);
            applicant.setSecondSubjPoints(secondSubj);
            applicant.setOnPaidBase(isPaid);
            applicant.setBirthday(birthday);

            Param param = new Param();
            param.addParameter(ParamName.APPLICANT, applicant);
            Controller.getInstance().doCommand(commandName, param);

            if (paneMode == PaneMode.ADD) {
                clear();
            } else {
                close();
            }
        } catch (DateTimeException ex) {
            new MessageBox("Ошибка ввода даты");
            return;
        } catch (EmptyFieldException ex) {
            new MessageBox("Ошибка ввода данных");
            return;
        } catch (NumberFormatException ex) {
            new MessageBox("Ошибка ввода баллов");
            return;
        }
    }

    private void clear() {
        nameField.setText("");
        surnameField.setText("");
        patronymicField.setText("");
        langPoints.setText("");
        firstPoints.setText("");
        secondPoints.setText("");
        schoolPoints.setText("");
        isPaidCheckBox.setSelected(false);

        selectedFaculty = null;
        facultyChoiceBox.setValue("");

        specializationList.getChildren().clear();
        pointsPane.setVisible(false);
        SpecializationToggle.clear();
    }

    private void close() {
        if (onClose != null) {
            onClose.handle(new Event(Event.ANY));
        }
    }

    private String getText(TextField field) throws EmptyFieldException {
        if (field.getText().isEmpty()) {
            throw new EmptyFieldException();
        }
        return field.getText();
    }

    private int getInt(TextField field) throws EmptyFieldException, NumberFormatException {
        if (field.getText().isEmpty()) {
            throw new EmptyFieldException();
        }
        int result = Integer.parseInt(field.getText());
        if (result <= 0) throw new NumberFormatException();
        return result;
    }

    private enum PaneMode {
        EDIT,
        ADD
    }
}
