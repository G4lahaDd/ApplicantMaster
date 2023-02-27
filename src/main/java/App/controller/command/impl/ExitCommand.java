package App.controller.command.impl;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.exception.CommandException;
import App.model.service.DBService;
import App.model.service.exception.ServiceException;
import App.view.Main;
import javafx.application.Application;

public class ExitCommand implements Command {
    public void execute(Param params) throws CommandException {
        System.out.println("Exit");
        try{
            DBService.getInstance().closeConnection();
            Main.getCurrentWindow().close();
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());
        }
        System.exit(0);
    }
}
