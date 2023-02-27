package App.view.controls;

import App.controller.command.Delegate;
import App.model.entity.Faculty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class FacultyToggle extends ToggleButton {
    private Faculty faculty;

    public FacultyToggle(Faculty faculty, Delegate onAction, ToggleGroup toggleGroup) {
        super();
        this.setToggleGroup(toggleGroup);
        //this.setText(faculty.getAbbreviation());
        this.setMaxWidth(Double.MAX_VALUE);
        StringProperty abbrProperty =faculty.getAbbrProperty();
        this.textProperty().bind(abbrProperty);
        this.faculty = faculty;
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onAction.execute((Object)faculty);
            }
        });
    }

    public Faculty getFaculty(){
        return faculty;
    }
}
