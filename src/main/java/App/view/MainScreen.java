package App.view;

import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.service.ApplicationDataService;
import App.view.controls.AddApplicantPane;
import App.view.controls.FacultyPane;
import App.view.controls.Refreshable;
import App.view.controls.SpecializationPane;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        faculties = ApplicationDataService.getInstance().getFaculties();
        FacultyPane facultyPane = new FacultyPane(faculties);
        facultyPane.setVisible(false);
        contentPane.getChildren().add(facultyPane);
        AnchorPane.setLeftAnchor(facultyPane,0d);
        AnchorPane.setRightAnchor(facultyPane,0d);
        AnchorPane.setBottomAnchor(facultyPane, 0d);
        AnchorPane.setTopAnchor(facultyPane, 0d);

        AddApplicantPane applicantPane = new AddApplicantPane();
        applicantPane.setVisible(false);
        contentPane.getChildren().add(applicantPane);
        AnchorPane.setLeftAnchor(applicantPane,0d);
        AnchorPane.setRightAnchor(applicantPane,0d);
        AnchorPane.setBottomAnchor(applicantPane, 0d);
        AnchorPane.setTopAnchor(applicantPane, 0d);

        pages = new HashMap<>();
        pages.put(PagesName.FACULTIES, facultyPane);
        pages.put(PagesName.APPLICANTS, applicantPane);

        applicantToggle.setOnAction(EventHandler -> selectPage(PagesName.APPLICANTS));
        facultyToggle.setOnAction(EventHandler -> selectPage(PagesName.FACULTIES));

        applicantToggle.setSelected(true);
        applicantPane.setVisible(true);
        /*contentPane.getChildren().add(pane);
        AnchorPane.setLeftAnchor(pane,0d);
        AnchorPane.setRightAnchor(pane,0d);*/
        //Добивать все
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

    @Override
    public void close() {
        if(mainWindow == null) return;
        mainWindow.close();
    }

    @Override
    public Stage getStage(){
        return mainWindow;
    }

    private class PagesName{
        private static final String FACULTIES = "faculties";
        private static final String GROUPS = "groups";
        private static final String APPLICANTS = "applicants";
    }
}
