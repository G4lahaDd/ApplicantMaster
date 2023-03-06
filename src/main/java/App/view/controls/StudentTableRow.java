package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.service.ApplicationDataService;
import App.view.EditApplicantWindow;
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

public class StudentTableRow extends GridPane implements Initializable, Refreshable {
    @FXML
    private Label initials;
    @FXML
    private Label birthday;
    @FXML
    private Label points;
    @FXML
    private Label isPaid;


    private Applicant applicant;
    private static byte[] xml;

    public StudentTableRow(Applicant applicant){
        super();
        this.applicant = applicant;
        load();
        refresh();
    }

    private void load(){
        final FXMLLoader loader = new FXMLLoader();
        try {
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

    @Override
    public void refresh() {
        initials.setText(applicant.getInitials());
        birthday.setText(applicant.getBirthday().toString());
        points.setText(Integer.toString(applicant.getTotalMark()));
        isPaid.setText(applicant.getOnPaidBase() ? "П" : "Б");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
