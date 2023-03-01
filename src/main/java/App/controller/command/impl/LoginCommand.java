package App.controller.command.impl;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.exception.CommandException;
import App.model.service.ApplicationDataService;
import App.model.service.exception.ServiceException;
import App.view.MessageBox;
import javafx.event.Event;
import javafx.event.EventHandler;

public class LoginCommand implements Command {

    @Override
    public void execute(Param params) throws CommandException {
        ApplicationDataService service = ApplicationDataService.getInstance();
        String login = (String) params.getParameter(ParamName.LOGIN);
        String password = (String) params.getParameter(ParamName.PASSWORD);
        if (login == null || login.isEmpty()) {
            return;
        }
        if (password == null || password.isEmpty()) {
            return;
        }
        try {
            if (service.login(login, password)) {
                EventHandler handler = (EventHandler) params.getParameter(ParamName.RESPONSE);
                if (handler != null) handler.handle(new Event(Event.ANY));
            } else {
                new MessageBox("Неверное имя пользователя или пароль");
            }
        } catch (ServiceException ex) {
            System.out.println("failed to login: " + ex.getMessage());
            throw new CommandException("Error while login");
        }
    }
}
