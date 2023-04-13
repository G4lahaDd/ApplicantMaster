package App.view;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Faculty;
import App.view.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
    private HBox menu;
    @FXML
    private Button backButton;

    @FXML
    private AnchorPane contentPane;

    private static final Controller controller = Controller.getInstance();
    private static Stage mainWindow = null;
    private List<Faculty> faculties;

    private Map<String, Node> pages;

    public MainScreen(){
        Param param = new Param();
        param.addParameter(ParamName.WINDOW, this);
        Controller.getInstance().doCommand("set-current-window", param);
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
        Param returnParam = new Param();
        controller.doCommand("load-faculties",returnParam);
        faculties = (List<Faculty>)returnParam.getParameter(ParamName.RETURN);
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
