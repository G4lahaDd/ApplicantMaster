package App.view;

import App.controller.Controller;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application{

    private static Window currentWindow;
    private static final Thread asyncLoad = new Thread(){
        @Override
        public void run() {
            Controller.getInstance().doCommand("Init");
        }
    };
    @Override
    public void start(Stage stage) throws Exception {
        asyncLoad.start();
        SplashScreen.create(stage);

    }

    public static void joinLoadThread(){
        try {
            if(asyncLoad.isAlive())
                asyncLoad.join();
        }
        catch(InterruptedException ex){
            System.out.println("Error while wait resource load: " + ex.getMessage());
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    public static Window getCurrentWindow() {
        return currentWindow;
    }

    public static void setCurrentWindow(Window currentWindow) {
        Main.currentWindow = currentWindow;
    }
}
