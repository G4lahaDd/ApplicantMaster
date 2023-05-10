package App.controller.command.impl.general;

import App.controller.command.Param;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.service.ApplicationDataService;
import App.model.service.exception.NoConnectionException;
import App.model.service.exception.ServiceException;

/**
 * Команда с инициализацией данных программы
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class InitCommand extends RemoteCommand {

    @Override
    public void executeRemote(Param params) throws CommandException, ServiceException {
        try {
            ApplicationDataService.getInstance().InitData();
        }
        catch (NoConnectionException ex){
            Thread.currentThread().interrupt();
            System.out.println("Please, try reconnect: " + ex.getMessage());
        }
    }
}
