package App.controller.command.impl.management;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.exception.CommandException;
import App.model.entity.Faculty;
import App.model.service.ApplicationDataService;

import java.util.List;

public class LoadFacultiesCommand implements Command {
    private static final ApplicationDataService service = ApplicationDataService.getInstance();

    @Override
    public void execute(Param params) throws CommandException {
        if(params == null) return;
        List<Faculty> faculties = service.getFaculties();
        params.addParameter(ParamName.RETURN, faculties);
    }
}
