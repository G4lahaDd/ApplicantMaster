package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.view.exception.EmptyFieldException;
import App.view.MessageBox;
import App.view.Parser;
import javafx.event.Event;
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

/**
 * Класс, описывающий панель для входа пользователя в систему
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class LoginPane extends GridPane implements Initializable {
    //region Компоненты
    @FXML
    private Button btnLogin;
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField pfPassword;
    //endregion
    private EventHandler onSuccessLogin;

    /**
     * Конструктор, инициализирующий панель для входа
     * @param onSuccessLogin Событие, вызываемое при успешном входе
     */
    public LoginPane(EventHandler onSuccessLogin){
        super();
        load();
        this.onSuccessLogin = onSuccessLogin;
    }

    /**
     * Загрузка графических компонентов из ресурсов
     */
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

    /**
     * Инициализация графических компонентов панели
     *
     * @param url            Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnLogin.setOnAction(EventHandler -> login());
    }

    /**
     * Логинация пользователя
     */
    private void login(){
        try {
            String login = Parser.getText(tfLogin);
            String password = Parser.getText(pfPassword);
            //Вызов логинации
            Param param = new Param();
            param.addParameter(ParamName.LOGIN, login);
            param.addParameter(ParamName.PASSWORD, password);
            Controller.getInstance().doCommand("login", param);
            //Получение результата
            Boolean result = (Boolean) param.getParameter(ParamName.RETURN);
            if(result){
                onSuccessLogin.handle(new Event(Event.ANY));
            }else{
                new MessageBox("Неверное имя пользователя или пароль");
            }
        }catch (EmptyFieldException ex){
            new MessageBox("Введите логин и пароль");
        }
    }
}
