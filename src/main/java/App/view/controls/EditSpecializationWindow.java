package App.view.controls;

import App.controller.Controller;
import App.controller.command.Container;
import App.model.entity.Specialization;
import App.model.entity.Subject;
import App.view.MainScreen;
import App.view.MessageBox;
import App.view.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditSpecializationWindow implements Initializable {
    private Container<Specialization> specialization;
    private static byte[] xml;
    private Stage stage;
    @FXML
    private TextField specializationCode;
    @FXML
    private TextField groupCode;
    @FXML
    private TextField specializationName;
    @FXML
    private TextField budgetPlaces;
    @FXML
    private TextField paidPlaces;
    @FXML
    private ChoiceBox<Subject> firstSubject;
    @FXML
    private ChoiceBox<Subject> secondSubject;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;

    private static final Controller controller = Controller.getInstance();

    private static final ObservableList<Subject> subjects = FXCollections.observableArrayList(Subject.values());

    public EditSpecializationWindow(Container<Specialization> specialization) {
        this.specialization = specialization;
        load();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstSubject.setItems(subjects);
        firstSubject.setValue(subjects.get(0));
        secondSubject.setItems(subjects);
        secondSubject.setValue(subjects.get(1));

        saveButton.setOnAction(EventHandler -> save());
        closeButton.setOnAction(EventHandler -> close());
    }

    private void load() {
        final FXMLLoader loader = new FXMLLoader();
        try {
            if (xml == null) {
                xml = getClass().getResource("EditSpecializationWindow.fxml")
                        .openStream()
                        .readAllBytes();
            }
            loader.setController(this);
            Parent root = loader.load(new ByteArrayInputStream(xml));
            Scene scene = new Scene(root, 600, 300);
            scene.getStylesheets().add(MainScreen.class.getResource("style/style.css").toExternalForm());
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit specialization");

            Window window = (Window)controller.doReturnCommand("get-current-window");
            Stage InitWindow = window.getStage();

            stage.initOwner(InitWindow);
            stage.initModality(Modality.WINDOW_MODAL);

            update();

            stage.showAndWait();
        } catch (IOException ex) {
            System.out.println("Failed to load 'EditSpecializationWindow.fxml' component:\n" + ex.getMessage());
        }
    }

    private void update() {
        if (specialization.isEmpty()) return;
        Specialization spec = specialization.get();
        specializationCode.setText(spec.getCode());
        specializationName.setText(spec.getName());
        groupCode.setText(spec.getGroupCode().toString());
        budgetPlaces.setText(spec.getBudgedPlaces().toString());
        paidPlaces.setText(spec.getPaidPlaces().toString());
        firstSubject.setValue(spec.getFirstSubject());
        secondSubject.setValue(spec.getSecondSubject());
    }

    private void save() {
        String name = specializationName.getText();
        String code = specializationCode.getText();
        int groupCode = 0;
        int budgetPlacesCount = 0;
        int paidPlacesCount = 0;
        try {
            budgetPlacesCount = Integer.parseInt(budgetPlaces.getText());
            paidPlacesCount = Integer.parseInt(paidPlaces.getText());
            groupCode = Integer.parseInt(this.groupCode.getText());
        } catch (NumberFormatException ex) {
            //close();
            new MessageBox("Неверно введено количество мест или номер группы");
            return;
        }
        if (!isValid(name, code, groupCode, budgetPlacesCount, paidPlacesCount)) {
            new MessageBox("Неверно введены данные");
            return;
        }
        if(specialization.isEmpty()){
            specialization.set(new Specialization());
        }
        specialization.get().setName(name);
        specialization.get().setCode(code);
        specialization.get().setGroupCode(groupCode);
        specialization.get().setBudgedPlaces(budgetPlacesCount);
        specialization.get().setPaidPlaces(paidPlacesCount);
        specialization.get().setFirstSubject(firstSubject.getValue());
        specialization.get().setSecondSubject(secondSubject.getValue());
        close();
    }

    private void close() {
        stage.close();
    }

    private boolean isValid(String name, String code, int groupCode, int budgetPlaces, int paidPlaces) {
        if (name.isEmpty()
                && code.isEmpty()
                && groupCode < 1
                && budgetPlaces < 1
                && paidPlaces < 1
        ) return false;
        return true;
    }
}
