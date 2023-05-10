package App.controller.command.impl.general;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.exception.CommandException;
import App.model.service.ApplicationDataService;

/**
 * Команда для получение текущего окна программы
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class GetCurrentWindowCommand implements Command {
    private static final ApplicationDataService service = ApplicationDataService.getInstance();
    @Override
    public void execute(Param params) throws CommandException {
        if(params == null) return;
        Object window = service.getCurrentWindow();
        params.addParameter(ParamName.RETURN, window);
    }
}
