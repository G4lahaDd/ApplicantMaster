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

/**
 * Класс, описывающий окно для редактирования специальности
 *
 */
public class EditSpecializationWindow implements Initializable {
    private Container<Specialization> specialization;
    private static byte[] xml;
    private Stage stage;
    //region Компоненты
    @FXML
    private TextField tfSpecializationCode;
    @FXML
    private TextField tfGroupCode;
    @FXML
    private TextField tfSpecializationName;
    @FXML
    private TextField tfBudgetPlaces;
    @FXML
    private TextField tfPaidPlaces;
    @FXML
    private ChoiceBox<Subject> chbFirstSubject;
    @FXML
    private ChoiceBox<Subject> chbSecondSubject;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    //endregion
    private static final Controller controller = Controller.getInstance();

    private static final ObservableList<Subject> subjects = FXCollections.observableArrayList(Subject.values());

    /**
     * Конструктор создающий окно для редактирования специальности
     * @param specialization Специальность для редактирования
     */
    public EditSpecializationWindow(Container<Specialization> specialization) {
        this.specialization = specialization;
        load();
    }

    /**
     * Инициализация графических компонентов панели
     * @param url Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chbFirstSubject.setItems(subjects);
        chbFirstSubject.setValue(subjects.get(0));
        chbSecondSubject.setItems(subjects);
        chbSecondSubject.setValue(subjects.get(1));

        btnSave.setOnAction(EventHandler -> save());
        btnClose.setOnAction(EventHandler -> close());
    }

    /**
     * Загрузка графических компонентов панели из ресурсов
     */
    private void load() {
        final FXMLLoader loader = new FXMLLoader();
        try {
            //Если компонент уже загружался, повторная загрузка не требуется
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
            //Получение родительского окна
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

    /**
     * Обновление данных специальности
     */
    private void update() {
        if (specialization.isEmpty()) return;
        Specialization spec = specialization.get();
        tfSpecializationCode.setText(spec.getCode());
        tfSpecializationName.setText(spec.getName());
        tfGroupCode.setText(spec.getGroupCode().toString());
        tfBudgetPlaces.setText(spec.getBudgedPlaces().toString());
        tfPaidPlaces.setText(spec.getPaidPlaces().toString());
        chbFirstSubject.setValue(spec.getFirstSubject());
        chbSecondSubject.setValue(spec.getSecondSubject());
    }

    /**
     * Сохранение специальности
     */
    private void save() {
        String name = tfSpecializationName.getText();
        String code = tfSpecializationCode.getText();
        int groupCode = 0;
        int budgetPlacesCount = 0;
        int paidPlacesCount = 0;
        try {
            budgetPlacesCount = Integer.parseInt(tfBudgetPlaces.getText());
            paidPlacesCount = Integer.parseInt(tfPaidPlaces.getText());
            groupCode = Integer.parseInt(this.tfGroupCode.getText());
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
        specialization.get().setFirstSubject(chbFirstSubject.getValue());
        specialization.get().setSecondSubject(chbSecondSubject.getValue());
        close();
    }

    /**
     * Закрытие окна
     */
    private void close() {
        stage.close();
    }

    /**
     * Проварка данных на валидность
     * @param name Название специальности
     * @param code Код специальности
     * @param groupCode Код группы
     * @param budgetPlaces Количество бюджетных мест
     * @param paidPlaces Количество платных мест
     * @return true - если все данные верны, иначе false
     */
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
