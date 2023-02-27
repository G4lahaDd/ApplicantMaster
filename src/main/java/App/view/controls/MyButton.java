package App.view.controls;

import App.controller.Controller;
import javafx.beans.DefaultProperty;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MyButton extends Button {

    private String command;

    public MyButton() {
        super();
        setOnAction(actionEvent -> {onClick();});
        getStyleClass().add("custom-button");
    }

    public MyButton(String text, String command){
        super();
        setText(text);
        this.command = command;
        setOnAction(actionEvent -> {onClick();});
        getStyleClass().add("custom-button");
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    private void onClick(){
        Controller.getInstance().doCommand(command);
    }
}