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

public class ApplicantTableRow extends GridPane implements Initializable, Refreshable {
    @FXML
    private Label initials;
    @FXML
    private Label birthday;
    @FXML
    private Label faculty;
    @FXML
    private Label langPoints;
    @FXML
    private Label schoolPoints;
    @FXML
    private Label firstSubjPoints;
    @FXML
    private Label secondSubjPoints;
    @FXML
    private Label isPaid;
    @FXML
    private Button editButton;


    private Applicant applicant;

    private static final Controller controller = Controller.getInstance();
    private static byte[] xml;
    private static List<Faculty> faculties;

    public ApplicantTableRow(Applicant applicant){
        super();
        this.applicant = applicant;
        load();
        if(faculties == null){
            faculties = (List<Faculty>)controller.doReturnCommand("load-faculties");
        }
        refresh();
    }

    private void load(){
        final FXMLLoader loader = new FXMLLoader();
        try {
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

    @Override
    public void refresh() {
        initials.setText(applicant.getInitials());
        birthday.setText(applicant.getBirthday().toString());
        Optional<Faculty> optFaculty = faculties.stream().filter(x -> x.getId()
                .equals(applicant.getFacultyId())).findAny();
        if(optFaculty.isEmpty()) return;
        Faculty faculty = optFaculty.get();
        this.faculty.setText(faculty.getAbbreviation());
        langPoints.setText(applicant.getLanguagePoints().toString());
        schoolPoints.setText(applicant.getSchoolMark().toString());
        firstSubjPoints.setText(applicant.getFirstSubjPoints().toString());
        secondSubjPoints.setText(applicant.getSecondSubjPoints().toString());
        isPaid.setText(applicant.getOnPaidBase() ? "П" : "Б");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editButton.setOnAction(EventHandler -> edit());
    }

    private void edit(){
        new EditApplicantWindow(applicant);
        refresh();
    }

    public void delete(){
        Param params = new Param();
        params.addParameter(ParamName.APPLICANT, applicant);
        Controller.getInstance().doCommand("delete-applicant",params);
    }
}
