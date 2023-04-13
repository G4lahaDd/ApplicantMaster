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

public class OpenMainWindowCommand implements Command {
    @Override
    public void execute(Param params) throws CommandException {
        Thread asyncLoad = ApplicationDataService.getInstance().getLoadThread();
        if(asyncLoad.isInterrupted()){
            Param param = new Param();
            Container<Boolean> container = new Container<>(false);
            param.addParameter(ParamName.BOOL_CONTAINER, container);
            Controller.getInstance().doCommand("reconnect", param);
            if(!container.get()){
                System.exit(0);
            }
        }
        Main.joinLoadThread();
        ApplicationDataService.getInstance().getCurrentWindow().close();
        new MainScreen();
    }
}
