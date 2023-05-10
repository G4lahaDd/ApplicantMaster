package App.view;

import App.controller.Controller;
import App.model.service.ApplicationDataService;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Класс с которого начинается запуск программы
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class Main extends Application{


    private static final Thread asyncLoad = new Thread(){
        @Override
        public void run() {
            Controller.getInstance().doCommand("Init");
        }
    };

    /**
     * Запуск приложения
     * @param stage Окно с которого запускается программа
     */
    @Override
    public void start(Stage stage) throws Exception {
        //Запуск потока с загрузкой необходимых приложению данных
        asyncLoad.start();
        ApplicationDataService.getInstance().setLoadThread(asyncLoad);
        SplashScreen.create(stage);
    }

    /**
     * Ожидание выполнения потока с загрузкой данных
     */
    public static void joinLoadThread(){
        try {
            if(asyncLoad.isAlive())
                asyncLoad.join();
        }
        catch(InterruptedException ex){
            System.out.println("Error while wait resource load: " + ex.getMessage());
        }
    }

    /**
     * Метод с которого запускается приложение
     * @param args параметры запуска
     */
    public static void main(String[] args){
            launch(args);
    }
}
