package App.controller.command.impl.management.applicant;

import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.entity.Applicant;
import App.model.service.ApplicantService;
import App.model.service.exception.ServiceException;

/**
 * Команда для удаления абитуриента
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class DeleteApplicantCommand extends RemoteCommand {
    private static final ApplicantService service = ApplicantService.getInstance();

    @Override
    public void executeRemote(Param params) throws CommandException, ServiceException{
        if (params == null) return;
        Object object = params.getParameter(ParamName.APPLICANT);
        if (object != null
                && object instanceof Applicant) {
                service.deleteApplicant( (Applicant) object);
        }
    }
}
