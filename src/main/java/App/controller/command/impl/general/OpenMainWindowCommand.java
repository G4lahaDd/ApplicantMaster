package App.controller.command.impl.general;

import App.controller.Controller;
import App.controller.command.Command;
import App.controller.command.Container;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.exception.CommandException;
import App.model.service.ApplicationDataService;
import App.view.Main;
import App.view.MainScreen;

/**
 * Команда открытия главного окна программы
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class OpenMainWindowCommand implements Command {
    private static final ApplicationDataService service = ApplicationDataService.getInstance();
    @Override
    public void execute(Param params) throws CommandException {
        Thread asyncLoad = ApplicationDataService.getInstance().getLoadThread();
        //Проверка потока с загрузкой данных на ошибку загрузки
        if(asyncLoad.isInterrupted()){
            Param param = new Param();
            Container<Boolean> container = new Container<>(false);
            param.addParameter(ParamName.BOOL_CONTAINER, container);
            //Попытка переподключения к сети
            Controller.getInstance().doCommand("reconnect", param);
            if(!container.get()){
                System.exit(0);
            }
            //Инициализация данных приложения при успешном переподключении
            Controller.getInstance().doCommand("init");
        }
        Main.joinLoadThread();
        service.getCurrentWindow().close();
        if(service.getMainWindow() != null){
            service.getMainWindow().getStage().show();
        }
        else new MainScreen();
    }
}
