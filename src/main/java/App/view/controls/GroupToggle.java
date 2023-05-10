package App.view.controls;

import App.model.entity.groups.Group;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/**
 * Класс, описывабщий кнопку-переключатель для групппы
 */
public class GroupToggle extends ToggleButton {
    private final Group group;
    private static final ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * Конструктор, инифиализирующий кнопку-переключатель
     * @param group Группа закреплённая за кнопкой
     * @param onAction Событие вызываемое на нажатие
     */
    public GroupToggle(Group group, EventHandler onAction){
        super();
        this.setToggleGroup(toggleGroup);
        this.setText(group.getCode());
        this.setMaxWidth(Double.MAX_VALUE);
        this.group = group;
        this.setOnAction(EventHandler->{
            onAction.handle(new Event(group, Event.NULL_SOURCE_TARGET, Event.ANY));
        });
    }

    /**
     * Получение группы закреплённой за кнопкой
     * @return Группа
     */
    public Group getGroup() {
        return group;
    }
}
