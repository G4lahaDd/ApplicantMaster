package App.controller.command.impl.general;

import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.service.ApplicationDataService;
import App.model.service.exception.ServiceException;

/**
 * Команда входа в пользователя
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class LoginCommand extends RemoteCommand {
    private static final ApplicationDataService service = ApplicationDataService.getInstance();

    @Override
    public void executeRemote(Param params) throws CommandException, ServiceException {
        String login = (String) params.getParameter(ParamName.LOGIN);
        String password = (String) params.getParameter(ParamName.PASSWORD);
        if (login == null || login.isEmpty()
                || password == null || password.isEmpty()) {
            params.addParameter(ParamName.RETURN, false);
            return;
        }
        //результат логинации
        params.addParameter(ParamName.RETURN, service.login(login, password));
    }
}
