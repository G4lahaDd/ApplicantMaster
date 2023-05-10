package App.controller.command.impl.general;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.exception.CommandException;
import App.model.service.DBService;
import App.model.service.exception.ServiceException;

/**
 * Команда выхода из приложения
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ExitCommand implements Command {
    public void execute(Param params) throws CommandException {
        System.out.println("Exit");
        try{
            //Закрытие подключения к БД
            DBService.getInstance().closeConnection();
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());
        }
        System.exit(0);
    }
}
