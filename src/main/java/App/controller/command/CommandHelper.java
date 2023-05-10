package App.controller.command;

import App.controller.command.impl.UnknownCommand;

/**
 * Класс - фабрика команд для получения экземпляров необходимых команд
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class CommandHelper {

    /**
     * Единственный экземпляр класса
     */
    private static final CommandHelper INSTANCE = new CommandHelper();

    /**
     * Приватный конструктор класса,
     * Создать экземпляр класса можно только внутри
     */
    private CommandHelper(){}

    /**
     * Возвращает единственный экземпляр класса
     * @return {@link #INSTANCE}
     */
    public static CommandHelper getInstance(){
        return INSTANCE;
    }

    /**
     * Возвращает неоходимую команду по её имени
     * @param commandName имя команды
     * @return команда
     */
    public Command getCommandByName(String commandName){
        Command command = null;
        CommandName key = null;

        if(commandName != null){
        commandName = commandName.replace('-', '_').toUpperCase();
        try{
            //получение команды из перечисления
            key = CommandName.valueOf(commandName);
            command = key.getCurrentCommand(); //получение команды из перечесления
        }catch (Exception ex){}
        }

        if(command == null){
            command = new UnknownCommand();
        }

        return command;
    }
}
