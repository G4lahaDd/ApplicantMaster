package App.controller;

import App.controller.command.Command;
import App.controller.command.CommandHelper;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.exception.CommandException;

public class Controller {

    private static final Controller INSTANSE = new Controller();
    private Controller(){}

    public static Controller getInstance(){
        return INSTANSE;
    }

    public void doCommand(String commandName){
        CommandHelper commandHelper = CommandHelper.getInstance();
        Command command = commandHelper.getCommandByName(commandName);

        try{
            command.execute(null);
        }
        catch(CommandException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void doCommand(String commandName, Param params){
        CommandHelper commandHelper = CommandHelper.getInstance();
        Command command = commandHelper.getCommandByName(commandName);

        try{
            command.execute(params);
        }
        catch(CommandException ex){
            System.out.println(ex.getMessage());
        }
    }

    public Object doReturnCommand(String commandName){
        CommandHelper commandHelper = CommandHelper.getInstance();
        Command command = commandHelper.getCommandByName(commandName);

        try{
            Param returnParam = new Param();
            command.execute(returnParam);
            if(returnParam.getParameter(ParamName.RETURN) == null)
                throw new CommandException("Команда " + commandName + " ничего не возвращает");
            return returnParam.getParameter(ParamName.RETURN);
        }
        catch(CommandException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
