package App.controller.command.impl.general;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.exception.CommandException;
import App.model.service.ApplicationDataService;
import App.view.MainScreen;
import App.view.Window;

/**
 * Команда установления текущего окна программы
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class SetCurrentWindowCommand implements Command {
    private static final ApplicationDataService service = ApplicationDataService.getInstance();
    @Override
    public void execute(Param params) throws CommandException {
        if(params == null) return;
        Window window = (Window)params.getParameter(ParamName.WINDOW);
        if(window instanceof MainScreen){
            service.setMainWindow(window);
        }
        service.setCurrentWindow(window);
    }
}
