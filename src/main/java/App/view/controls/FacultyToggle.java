package App.view.controls;

import App.model.entity.Faculty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/**
 * Класс для переключателя выбранного факультета
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class FacultyToggle extends ToggleButton {
    private Faculty faculty;

    /**
     * Конструктор инициализирующий переключатель для факультета
     * @param faculty Факультет
     * @param onAction Событие вызываемое на нажатие
     * @param toggleGroup Группа, к которой относится переключаемая кнопка
     */
    public FacultyToggle(Faculty faculty, EventHandler onAction, ToggleGroup toggleGroup) {
        super();
        this.setToggleGroup(toggleGroup);
        //this.setText(faculty.getAbbreviation());
        this.setMaxWidth(Double.MAX_VALUE);
        StringProperty abbrProperty =faculty.getAbbrProperty();
        this.textProperty().bind(abbrProperty);
        this.faculty = faculty;
        this.setOnAction(e -> onAction.handle(new Event(faculty, Event.NULL_SOURCE_TARGET, Event.ANY)));
    }

    /**
     * Получение факультета, закреплённого за кнопкой
     * @return Факультет
     */
    public Faculty getFaculty(){
        return faculty;
    }
}
