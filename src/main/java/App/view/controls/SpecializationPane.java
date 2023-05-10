package App.view.controls;

import App.controller.command.Container;
import App.model.entity.Specialization;
import App.view.ConfirmBox;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Класс, описывающий панель для отображения специальности
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class SpecializationPane extends GridPane implements Initializable{
    private Specialization specialization;
    private static byte[] xml;
    //region Компоненты
    @FXML
    private Label lblSpecializationCode;
    @FXML
    private Label lblSpecializationName;
    @FXML
    private Label lblBudgetPlaces;
    @FXML
    private Label lblPaidPlaces;
    @FXML
    private Label lblFirstSubject;
    @FXML
    private Label lblSecondSubject;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    //endregion
    private EventHandler onUpdateMethod;
    private static EventHandler onDeleteMethod;


    /**
     * Конструктор, инициализирующий панель специальности
     * @param specialization Закреплённая за панелью специальность
     * @param onUpdateMethod Событие вызываемое при изменении специальности
     */
    public SpecializationPane(Specialization specialization, EventHandler onUpdateMethod){
        super();
        load();
        this.specialization = specialization;
        this.onUpdateMethod = onUpdateMethod;
        update();
    }

    /**
     * Загрузка графических компонентов из ресурсов
     */
    private void load(){
        final FXMLLoader loader = new FXMLLoader();
        try {
            //Если компоненты загружались из ресурсов, повторная загрузка не требуется
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

    /**
     * Инициализация графических компонентов панели
     *
     * @param url            Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEdit.setOnAction(EventHandler -> edit());
        btnDelete.setOnAction(EventHandler -> delete());
    }

    /**
     * Обновление текстовый данный на экране
     */
    private void update(){
        if(specialization == null) return;
        lblSpecializationCode.setText(specialization.getCode());
        lblSpecializationName.setText(specialization.getName());
        lblBudgetPlaces.setText(specialization.getBudgedPlaces().toString());
        lblPaidPlaces.setText(specialization.getPaidPlaces().toString());
        lblFirstSubject.setText(specialization.getFirstSubject().toString());
        lblSecondSubject.setText(specialization.getSecondSubject().toString());

    }

    /**
     * Изменение специальности
     */
    private void edit(){
        //Создание контейнера для передачи значения по ссылке
        Container<Specialization> specializationContainer = new Container<>(specialization);
        //Создание окна
        new EditSpecializationWindow(specializationContainer);
        //Проверка результатов
        if(specializationContainer.isEmpty()) {
            new MessageBox("Ошибка введённых данных");
            return;
        }
        specialization = specializationContainer.get();
        update();
        onUpdateMethod.handle(new Event(Event.ANY));
    }

    /**
     * Удаление специальности
     */
    private void delete(){
        if(!ConfirmBox.Show("Вы уверены что хотите удалить специальность?")){
            return;
        }
        if(onDeleteMethod != null) {
            onDeleteMethod.handle(new Event(this, Event.NULL_SOURCE_TARGET, Event.ANY));
        }
        /*Param param = new Param();
        param.addParameter(ParamName.SPECIALIZATION, specialization);
        Controller.getInstance().doCommand("delete-specialization",param);*/
    }

    /**
     * Установление события, вызываемого при удалении специальности
     * @param onDeleteMethod Событие, вызываемое при удалении специальности
     */
    public static void setOnDelete(EventHandler onDeleteMethod) {
        SpecializationPane.onDeleteMethod = onDeleteMethod;
    }

    /**
     * Получение специальности, закреплённой за панелью
     * @return Специальность
     */
    public Specialization getSpecialization(){
        return specialization;
    }
}
