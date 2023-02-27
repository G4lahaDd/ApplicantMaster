package App.controller.command;

import App.controller.command.impl.UnknownCommand;

import java.util.Locale;

public class CommandHelper {

    private static final CommandHelper INSTANCE = new CommandHelper();
    private CommandHelper(){}

    public static CommandHelper getInstance(){
        return INSTANCE;
    }

    public Command getCommandByName(String commandName){
        Command command = null;
        CommandName key = null;

        if(commandName != null){
        commandName = commandName.replace('-', '_').toUpperCase();
        try{
            key = CommandName.valueOf(commandName);
            command = key.getCurrentCommand();
        }catch (Exception ex){}
        }

        if(command == null){
            command = new UnknownCommand();
        }

        return command;
    }
}
