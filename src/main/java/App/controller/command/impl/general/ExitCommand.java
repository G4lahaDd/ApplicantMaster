package App.controller.command.impl.general;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.exception.CommandException;
import App.model.service.DBService;
import App.model.service.exception.ServiceException;

public class ExitCommand implements Command {
    public void execute(Param params) throws CommandException {
        System.out.println("Exit");
        try{
            DBService.getInstance().closeConnection();
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());
        }
        System.exit(0);
    }
}
