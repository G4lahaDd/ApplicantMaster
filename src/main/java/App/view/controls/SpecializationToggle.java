package App.view.controls;

import App.model.entity.Specialization;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Класс, описывающий кнопку-переключатель для выбора приоритета специальностей
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class SpecializationToggle extends AnchorPane implements Initializable {
    //region Компоненты
    @FXML
    private Label lblTitle;
    @FXML
    private ToggleButton toggle;
    //endregion

    private static byte[] xml;
    private final Specialization specialization;
    private static final SpecToggleGroup specToggleGroup = new SpecToggleGroup();
    /**
     * приоритет специальности, если она выбрана, иначе -1
     */
    private int index;


    /**
     * Конструктор, инициализирующий кнопку-выключатель для специальности
     * @param specialization специальность, закреплённая за кнопкой
     */
    public SpecializationToggle(Specialization specialization) {
        super();
        index = -1;
        this.specialization = specialization;
        load();
        lblTitle.setText(specialization.getName());
    }

    /**
     * Загрузка графических компонентов из ресурсов
     */
    private void load() {
        final FXMLLoader loader = new FXMLLoader();
        try {
            //Если граф. компоненты уже загружались, загрузка не требуется
            if (xml == null) {
                xml = getClass().getResource("SpecializationToggle.fxml")
                        .openStream()
                        .readAllBytes();
            }
            loader.setController(this);
            Node node = loader.load(new ByteArrayInputStream(xml));
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'SpecializationToggle.fxml' component: " + ex.getMessage());
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
        toggle.setText("-");
        toggle.setOnAction(EventHandler -> action());
    }

    /**
     * Выбор специальность
     */
    public void select(){
        toggle.setSelected(true);
        action();
    }

    /**
     * Действие, вызываемое при взаимодействии с кнопкой-выключателем
     */
    private void action() {
        if(index < 0) specToggleGroup.add(this);
        else specToggleGroup.remove(this);
    }

    /**
     * Обновление текста приоритета
     */
    private void updateText() {
        toggle.setText(index > 0 ? Integer.toString(index) : "-");
    }

    /**
     * Получение приоритета специальности
     * @return Приоритет специальности (Индекс)
     */
    public int getIndex() {
        return index;
    }

    /**
     * Получение закреплённой за кнопкой специальности
     * @return специальность
     */
    public Specialization getSpecialization(){
        return specialization;
    }

    /**
     * Очистка выбранного списка
     */
    public static void clear(){
        specToggleGroup.clear();
    }

    /**
     * Установка события вызываемого на выбор первого элемента
     * @param onChooseFirst Событие вызываемое на выбор первого элемента
     */
    public static void setOnChooseFirst(EventHandler onChooseFirst) {
        specToggleGroup.onChooseFirst = onChooseFirst;
    }

    /**
     * Установка события вызываемого на удаление последнего выбранного элемента из списка приоритетных специальностей
     * @param onLostFirst Событие вызываемое на удаление первого элемента
     */
    public static void setOnLostFirst(EventHandler onLostFirst) {
        specToggleGroup.onLostFirst = onLostFirst;
    }

    /**
     * Класс для описания группы, к которым относятся все кнопки-переключатели
     *
     * @author Kazyro I.A.
     * @version 1.0
     */
    private static class SpecToggleGroup {
        private EventHandler onChooseFirst;
        private EventHandler onLostFirst;

        List<SpecializationToggle> toggleList = new ArrayList<>();
        int next = 1;

        /**
         * Добавление кнопки-переключателя специальности
         * @param toggle кнопка-переключатель
         */
        void add(SpecializationToggle toggle) {
            //Проверка на выбор первой кнопки
            if(next == 1 && onChooseFirst != null){
                Event event = new Event(toggle, Event.NULL_SOURCE_TARGET, Event.ANY);
                onChooseFirst.handle(event);
            }
            //Установка приоритета кнопки
            toggle.index = next++;
            toggle.updateText();
            toggleList.add(toggle);

        }

        /**
         * Удаление кнопки-переключателя специальности
         * @param toggle кнопка-переключатель
         */
        void remove(SpecializationToggle toggle) {
            //Сдвиг всех индексов, которые идут после удаляемого
            for(int i = 0; i < toggleList.size(); i++){
                if(toggleList.get(i).index > toggle.index){
                    toggleList.get(i).index--;
                    toggleList.get(i).updateText();
                }
            }
            toggleList.remove(toggle);
            toggle.index = -1;
            toggle.updateText();
            next--;
            //потеря последнего выбранного
            if(next == 1 && onLostFirst != null){
                onLostFirst.handle(new Event(Event.ANY));
            }
        }

        /**
         * Очистка выбранных кнопок-переключателей
         */
        void clear(){
            for(int i = 0; i < toggleList.size(); i++){
                toggleList.get(i).index = -1;
                toggleList.get(i).updateText();
            }
            toggleList.clear();
            next = 1;
        }
    }
}
