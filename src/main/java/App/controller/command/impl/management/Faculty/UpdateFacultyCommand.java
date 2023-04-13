package App.controller.command.impl.management.Faculty;

import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.entity.Faculty;
import App.model.service.FacultyService;
import App.model.service.exception.ServiceException;
import App.view.MessageBox;

public class UpdateFacultyCommand extends RemoteCommand {
    private static final FacultyService service = FacultyService.getInstance();

    @Override
    public void executeRemote(Param params) throws CommandException, ServiceException {
        if (params == null) return;
        Object object = params.getParameter(ParamName.FACULTY);
        if (object != null
                && object instanceof Faculty) {
                System.out.println("Update faculty command");
                boolean result = service.updateFaculty((Faculty) object);
                if(!result){
                    new MessageBox("Не удалось обновить факультет");
                }
        }
    }
}