package App.controller.command.impl.management.applicant;

import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.entity.Applicant;
import App.model.service.ApplicantService;
import App.model.service.exception.ServiceException;
import App.view.MessageBox;

/**
 * Команда для добавления абитуриента
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class AddApplicantCommand extends RemoteCommand {
    private static final ApplicantService service = ApplicantService.getInstance();

    @Override
    public void executeRemote(Param params) throws CommandException, ServiceException {
        if (params == null) return;
        Object object = params.getParameter(ParamName.APPLICANT);
        if (!(object instanceof Applicant) || object == null) return;

        boolean result = service.addApplicant((Applicant) object);
        if (!result) {
            new MessageBox("Не удалось добавить абитуриента");
        }
    }
}
