package App.controller.command.impl.management.Faculty;

import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.entity.Faculty;
import App.model.service.ApplicationDataService;
import App.model.service.FacultyService;
import App.model.service.exception.ServiceException;

/**
 * Команда для удаления факультета
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class DeleteFacultyCommand extends RemoteCommand {
    private static final FacultyService service = FacultyService.getInstance();

    @Override
    public void executeRemote(Param params) throws CommandException, ServiceException {
        if (params == null) return;
        Object object = params.getParameter(ParamName.FACULTY);
        if (object != null
                && object instanceof Faculty) {
            service.deleteFaculty((Faculty) object);
            ApplicationDataService.getInstance().removeFaculty((Faculty) object);
        }
    }
}