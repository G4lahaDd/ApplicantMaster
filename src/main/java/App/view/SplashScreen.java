package App.view;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс для стартового окна программы
 */
public class SplashScreen implements Window {
    private Timer timer;
    private static Stage splashWindow;

    /**
     * Загрузка компонентов из ресурсов в стартовое окно программы
     * @param stage Стартовое окно приложения
     */
    public static void create(Stage stage) {
        try {
            Parent root = FXMLLoader.load(SplashScreen.class.getResource("start.fxml"));
            stage.setTitle("Load screen");
            Scene scene = new Scene(root, 600, 600);
            scene.getStylesheets().add(SplashScreen.class.getResource("style/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            //Замена стандартного закрытия приложения на безопастное
            stage.setOnCloseRequest(e -> {
                Controller.getInstance().doCommand("exit");
            });
            splashWindow = stage;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Конструктор, инициализирующий стартовое окно программы
     */
    public SplashScreen() {
        //Создание таймера на выключение при бездействии
        TimerTask exitTask = new TimerTask() {
            @Override
            public void run() {
                Controller.getInstance().doCommand("exit");
            }
        };
        timer = new Timer();
        //Установка времени
        timer.schedule(exitTask, 60000);
        //Установка окна текущим
        Param param = new Param();
        param.addParameter(ParamName.WINDOW, this);
        Controller.getInstance().doCommand("set-current-window", param);
    }

    /**
     * Закрытие окна
     */
    @Override
    public void close() {
        timer.cancel();
        if (splashWindow != null) {
            splashWindow.close();
        }
    }

    @Override
    public Stage getStage() {
        return splashWindow;
    }
}
