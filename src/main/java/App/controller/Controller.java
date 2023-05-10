package App.controller;

import App.controller.command.Command;
import App.controller.command.CommandHelper;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.exception.CommandException;

/**
 * Класс отвечающий за связь слоя модели со слоем представления
 * Для получения экземпляра класса используется {@link #getInstance() метод}
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class Controller {
    /**
     * Единственный экземпляр класса
     */
    private static final Controller INSTANCE = new Controller();

    /**
     * Приватный конструктор класса,
     * Создать экземпляр класса можно только внутри
     */
    private Controller(){}

    /**
     * Возвращает единственный экземпляр класса
     * @return {@link #INSTANCE}
     */
    public static Controller getInstance(){
        return INSTANCE;
    }

    /**
     * Ваполняет команду
     * @param commandName имя команды
     */
    public void doCommand(String commandName){
        CommandHelper commandHelper = CommandHelper.getInstance();
        //Поиск нужной команды по имени
        Command command = commandHelper.getCommandByName(commandName);

        try{
            command.execute(null);
        }
        catch(CommandException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * выполняет команду с параметрами
     * @param commandName имя команды
     * @param params параметры команды
     */
    public void doCommand(String commandName, Param params){
        CommandHelper commandHelper = CommandHelper.getInstance();
        //поиск команды по имени
        Command command = commandHelper.getCommandByName(commandName);

        try{
            command.execute(params);
        }
        catch(CommandException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * выполняет команду, возвращающую значение
     * @param commandName имя команды
     * @return результат команды
     */
    public Object doReturnCommand(String commandName){
        CommandHelper commandHelper = CommandHelper.getInstance();
        //Поиск команды по имени
        Command command = commandHelper.getCommandByName(commandName);

        try{
            //Создание параметров для возврата значения из команды
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
