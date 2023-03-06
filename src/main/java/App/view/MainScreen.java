package App.view;

import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.entity.groups.Group;
import App.model.service.ApplicationDataService;
import App.view.controls.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainScreen extends GridPane implements Window, Initializable {
    @FXML
    private RadioButton applicantToggle;
    @FXML
    private RadioButton groupsToggle;
    @FXML
    private RadioButton facultyToggle;

    @FXML
    private HBox menu;
    @FXML
    private Button backButton;

    @FXML
    private AnchorPane contentPane;

    private static Stage mainWindow = null;
    private List<Faculty> faculties;

    private Map<String, Node> pages;

    public MainScreen(){
        Main.setCurrentWindow(this);
        load();
    }

    private void load() {
        if(mainWindow != null) return;
        try {
            mainWindow = new Stage();
            final FXMLLoader loader = new FXMLLoader(MainScreen.class.getResource("main.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            mainWindow.setMinHeight(500);
            mainWindow.setMinWidth(800);

            mainWindow.setTitle("ApplicantMaster");
            Scene scene = new Scene(root,900,600);
            scene.getStylesheets().add(MainScreen.class.getResource("style/style.css").toExternalForm());
            mainWindow.setScene(scene);
            mainWindow.show();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public <T extends Node> T fitPage(T page){
        page.setVisible(false);
        contentPane.getChildren().add(page);
        AnchorPane.setLeftAnchor(page,0d);
        AnchorPane.setRightAnchor(page,0d);
        AnchorPane.setBottomAnchor(page, 0d);
        AnchorPane.setTopAnchor(page, 0d);
        return page;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        LoginPane facultyPane = fitPage(new LoginPane(EventHandler -> successLogin()));
        AddApplicantPane addApplicantPane = fitPage(new AddApplicantPane());
        ApplicantsPane applicantPane = fitPage(new ApplicantsPane(EventHandler -> openAddApplicantPage()));
        GroupPane groupPane = fitPage(new GroupPane());

        pages = new HashMap<>();
        pages.put(PagesName.FACULTIES, facultyPane);
        pages.put(PagesName.ADD_APPLICANT, addApplicantPane);
        pages.put(PagesName.APPLICANTS, applicantPane);
        pages.put(PagesName.GROUPS, groupPane);

        applicantToggle.setOnAction(EventHandler -> selectPage(PagesName.APPLICANTS));
        facultyToggle.setOnAction(EventHandler -> selectPage(PagesName.FACULTIES));
        groupsToggle.setOnAction(EventHandler -> selectPage(PagesName.GROUPS));

        applicantToggle.setSelected(true);
        applicantPane.setVisible(true);

        backButton.setOnAction(EventHandler -> back());
    }

    private void selectPage(String pageName){
        for(Node node : pages.values()){
            node.setVisible(false);
        }
        Node selectedPage = pages.get(pageName);
        if(selectedPage instanceof Refreshable){
            ((Refreshable) selectedPage).refresh();
        }
        selectedPage.setVisible(true);
    }

    private void back(){
        menu.setVisible(true);
        backButton.setVisible(false);
        selectPage(PagesName.APPLICANTS);
    }

    private void openAddApplicantPage(){
        menu.setVisible(false);
        backButton.setVisible(true);
        selectPage(PagesName.ADD_APPLICANT);
    }

    @Override
    public void close() {
        if(mainWindow == null) return;
        mainWindow.close();
    }

    @Override
    public Stage getStage(){
        return mainWindow;
    }

    private void successLogin(){
        faculties = ApplicationDataService.getInstance().getFaculties();
        FacultyPane facultyPane = fitPage(new FacultyPane(faculties));
        Node node = pages.get(PagesName.FACULTIES);
        node.setVisible(false);
        contentPane.getChildren().remove(node);
        pages.replace(PagesName.FACULTIES, facultyPane);
        facultyPane.setVisible(true);
    }

    private class PagesName{
        private static final String FACULTIES = "faculties";
        private static final String GROUPS = "groups";
        private static final String APPLICANTS = "applicants";
        private static final String ADD_APPLICANT = "add_applicants";
    }
}
