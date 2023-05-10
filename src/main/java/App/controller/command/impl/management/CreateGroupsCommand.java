package App.controller.command.impl.management;

import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.entity.groups.FacultyThread;
import App.model.service.ApplicantService;
import App.model.service.exception.ServiceException;

import java.util.List;

/**
 * Команда создания списков групп
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class CreateGroupsCommand extends RemoteCommand {
    @Override
    protected void executeRemote(Param params) throws CommandException, ServiceException {
        if(params == null) return;
        ApplicantService service = ApplicantService.getInstance();
        List<FacultyThread> faculties = service.createGroups();
        params.addParameter(ParamName.RETURN, faculties);
    }
}
