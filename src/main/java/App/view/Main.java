package App.view;

import App.controller.Controller;
import App.model.service.ApplicationDataService;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    private static final Thread asyncLoad = new Thread(){
        @Override
        public void run() {
            Controller.getInstance().doCommand("Init");
        }
    };
    @Override
    public void start(Stage stage) throws Exception {
        asyncLoad.start();
        ApplicationDataService.getInstance().setLoadThread(asyncLoad);
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
}
