package App.view.controls;

import App.controller.Controller;
import App.controller.command.Container;
import App.controller.command.Delegate;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Specialization;
import App.view.MainScreen;
import App.view.MessageBox;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

public class SpecializationPane extends GridPane implements Initializable{
    private Specialization specialization;
    private static byte[] xml;

    @FXML
    private Label specializationCode;
    @FXML
    private Label specializationName;
    @FXML
    private Label budgetPlaces;
    @FXML
    private Label paidPlaces;
    @FXML
    private Label firstSubject;
    @FXML
    private Label secondSubject;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private static Delegate onDeleteMethod;



    public SpecializationPane(Specialization specialization){
        super();
        load();
        this.specialization = specialization;
        update();
    }

    private void load(){
        final FXMLLoader loader = new FXMLLoader();
        try {
            if(xml == null) {
                xml = getClass().getResource("SpecializationPane.fxml")
                        .openStream()
                        .readAllBytes();
            }
            loader.setController(this);
            Node node = loader.load(new ByteArrayInputStream(xml));
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'SpecializationPane.fxml' component:\n" + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editButton.setOnAction(EventHandler -> edit());
        deleteButton.setOnAction(EventHandler -> delete());
    }

    private void update(){
        if(specialization == null) return;
        specializationCode.setText(specialization.getCode());
        specializationName.setText(specialization.getName());
        budgetPlaces.setText(specialization.getBudgedPlaces().toString());
        paidPlaces.setText(specialization.getPaidPlaces().toString());
        firstSubject.setText(specialization.getFirstSubject().toString());
        secondSubject.setText(specialization.getSecondSubject().toString());
    }

    private void edit(){
        Container<Specialization> specializationContainer = new Container<>(specialization);
        new EditSpecializationWindow(specializationContainer);
        if(specializationContainer.isEmpty()) {
            new MessageBox("Ошибка введённых данных");
        }
        specialization = specializationContainer.get();
        update();
    }

    private void delete(){
        if(onDeleteMethod != null) {
            onDeleteMethod.execute(this);
        }
        Param param = new Param();
        param.addParameter(ParamName.SPECIALIZATION, specialization);
        Controller.getInstance().doCommand("delete-specialization",param);
    }

    public static void setOnDelete(Delegate onDeleteMethod) {
        SpecializationPane.onDeleteMethod = onDeleteMethod;
    }

    public Specialization getSpecialization(){
        return specialization;
    }
}
