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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Класс главного окна программы
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class MainScreen extends GridPane implements Window, Initializable {
    //region Компоненты
    @FXML
    private RadioButton tglApplicant;
    @FXML
    private RadioButton tglGroups;
    @FXML
    private RadioButton tglFaculty;

    @FXML
    private HBox menu;
    @FXML
    private Button btnBack;

    @FXML
    private AnchorPane pnContent;
    //endregion
    private static final Controller controller = Controller.getInstance();
    private static Stage mainWindow = null;
    private List<Faculty> faculties;

    private Map<String, Node> pages;

    /**
     * Конструктор инициализирующий главное окно программы
     */
    public MainScreen() {
        Param param = new Param();
        param.addParameter(ParamName.WINDOW, this);
        Controller.getInstance().doCommand("set-current-window", param);
        load();
    }

    /**
     * Загрузка графических компонентов окна из ресурсов
     */
    private void load() {
        if (mainWindow != null) return;
        try {
            mainWindow = new Stage();
            final FXMLLoader loader = new FXMLLoader(MainScreen.class.getResource("main.fxml"));
            loader.setController(this);
            //Создание меню
            MenuItem miAboutAuthor = new MenuItem("Об авторе");
            miAboutAuthor.setOnAction(e -> controller.doCommand("about-author"));
            MenuItem miAboutProgram = new MenuItem("О программе");
            miAboutProgram.setOnAction(e -> controller.doCommand("about-program"));
            MenuItem miExit = new MenuItem("Выход");
            miExit.setOnAction(e -> controller.doCommand("exit"));
            Menu mFile = new Menu("File");
            mFile.getStyleClass().add("filemenu");
            MenuBar menuBar = new MenuBar();
            menuBar.getMenus().add(mFile);
            mFile.getItems().addAll(miAboutAuthor, miAboutProgram, miExit);

            //Загрузка компонентов
            BorderPane root = new BorderPane();
            root.setTop(menuBar);
            root.setCenter(loader.load());

            mainWindow.setMinHeight(500);
            mainWindow.setMinWidth(800);

            mainWindow.setTitle("ApplicantMaster");
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(MainScreen.class.getResource("style/style.css").toExternalForm());
            mainWindow.setScene(scene);
            //Замена стандартного закрытия приложения на безопастное
            mainWindow.setOnCloseRequest(e -> {
                Controller.getInstance().doCommand("exit");
            });
            mainWindow.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Вписываение компонента по границам контейнера
     *
     * @param page Компонент для вписывания
     * @param <T>  Графические компоненты
     * @return Компонет после вписывания
     */
    public <T extends Node> T fitPage(T page) {
        page.setVisible(false);
        pnContent.getChildren().add(page);
        AnchorPane.setLeftAnchor(page, 0d);
        AnchorPane.setRightAnchor(page, 0d);
        AnchorPane.setBottomAnchor(page, 0d);
        AnchorPane.setTopAnchor(page, 0d);
        return page;
    }

    /**
     * Инициализация графических компонентов панели
     *
     * @param url            Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        MenuBar menuBar = new MenuBar();
        //Инициализация страниц
        LoginPane facultyPane = fitPage(new LoginPane(EventHandler -> successLogin()));
        AddApplicantPane addApplicantPane = fitPage(new AddApplicantPane());
        ApplicantsPane applicantPane = fitPage(new ApplicantsPane(EventHandler -> openAddApplicantPage()));
        GroupPane groupPane = fitPage(new GroupPane());
        //Добавление страниц в коллекцию
        pages = new HashMap<>();
        pages.put(PagesName.FACULTIES, facultyPane);
        pages.put(PagesName.ADD_APPLICANT, addApplicantPane);
        pages.put(PagesName.APPLICANTS, applicantPane);
        pages.put(PagesName.GROUPS, groupPane);

        tglApplicant.setOnAction(EventHandler -> selectPage(PagesName.APPLICANTS));
        tglFaculty.setOnAction(EventHandler -> selectPage(PagesName.FACULTIES));
        tglGroups.setOnAction(EventHandler -> selectPage(PagesName.GROUPS));

        tglApplicant.setSelected(true);
        applicantPane.setVisible(true);

        btnBack.setOnAction(EventHandler -> back());
    }

    /**
     * Выбор страницы по названию
     *
     * @param pageName название страницы
     */
    private void selectPage(String pageName) {
        for (Node node : pages.values()) {
            node.setVisible(false);
        }
        //Получение выбранной страницы
        Node selectedPage = pages.get(pageName);
        //Если возможно обновление страницы, обновляем данные
        if (selectedPage instanceof Refreshable) {
            ((Refreshable) selectedPage).refresh();
        }
        selectedPage.setVisible(true);
    }

    /**
     * Возврат на главную страницу
     */
    private void back() {
        menu.setVisible(true);
        btnBack.setVisible(false);
        selectPage(PagesName.APPLICANTS);
    }

    /**
     * Открытие страницы с добавлением абитуриента
     */
    private void openAddApplicantPage() {
        menu.setVisible(false);
        btnBack.setVisible(true);
        selectPage(PagesName.ADD_APPLICANT);
    }

    /**
     * Закрытие окна
     */
    @Override
    public void close() {
        if (mainWindow == null) return;
        mainWindow.close();
    }

    @Override
    public Stage getStage() {
        return mainWindow;
    }

    /**
     * Успешнный вход пользователя
     */
    private void successLogin() {
        Param returnParam = new Param();
        //Подгрузка факультетов
        controller.doCommand("load-faculties", returnParam);
        faculties = (List<Faculty>) returnParam.getParameter(ParamName.RETURN);
        FacultyPane facultyPane = fitPage(new FacultyPane(faculties));
        //Получение страницы с логинацией
        Node node = pages.get(PagesName.FACULTIES);
        node.setVisible(false);
        pnContent.getChildren().remove(node);
        //Замещение страницы с логинацией на страницу с факультетами
        pages.replace(PagesName.FACULTIES, facultyPane);
        facultyPane.setVisible(true);
    }

    /**
     * Класс констант с именами страниц
     *
     * @author Kazyro I.A.
     * @version 1.0
     */
    private static class PagesName {
        private static final String FACULTIES = "faculties";
        private static final String GROUPS = "groups";
        private static final String APPLICANTS = "applicants";
        private static final String ADD_APPLICANT = "add_applicants";
    }
}
