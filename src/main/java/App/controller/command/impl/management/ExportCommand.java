package App.controller.command.impl.management;

import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.service.ExelService;
import App.model.service.exception.ServiceException;

import java.io.File;

public class ExportCommand extends RemoteCommand {
    private static final ExelService service = ExelService.getInstance();

    @Override
    public void executeRemote(Param params) throws CommandException, ServiceException {
            if(params == null) return;
            Object object = params.getParameter(ParamName.FILE);
            if(object == null || !(object instanceof File)) return;
            service.export((File)object);
    }
}
