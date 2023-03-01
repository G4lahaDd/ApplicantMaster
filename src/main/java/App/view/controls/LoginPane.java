package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.view.Exception.EmptyFieldException;
import App.view.MessageBox;
import App.view.Parser;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPane extends GridPane implements Initializable {
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    private EventHandler onLogin;

    public LoginPane(EventHandler onLogin){
        super();
        load();
        this.onLogin = onLogin;
    }

    private void load(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPane.fxml"));
            loader.setController(this);
            Node node = loader.load();
            this.getChildren().add(node);
        }catch(IOException ex){
            System.out.println("Failed to load 'LoginPane.fxml' component:\n" + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(EventHandler -> login());
    }

    private void login(){
        try {
            String login = Parser.getText(loginField);
            String password = Parser.getText(passwordField);

            Param param = new Param();
            param.addParameter(ParamName.LOGIN, login);
            param.addParameter(ParamName.PASSWORD, password);
            param.addParameter(ParamName.RESPONSE, onLogin);
            Controller.getInstance().doCommand("login", param);
        }catch (EmptyFieldException ex){
            new MessageBox("Введите логин и пароль");
        }
    }
}
