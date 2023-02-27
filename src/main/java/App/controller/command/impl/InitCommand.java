package App.controller.command.impl;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.exception.CommandException;
import App.model.service.ApplicationDataService;
import App.model.service.exception.ServiceException;

public class InitCommand implements Command {

    @Override
    public void execute(Param params) throws CommandException {
        try {
            ApplicationDataService.getInstance().InitData();
        }
        catch (ServiceException ex){
            System.out.println("Init command exception: " + ex.getMessage());
        }
    }
}
