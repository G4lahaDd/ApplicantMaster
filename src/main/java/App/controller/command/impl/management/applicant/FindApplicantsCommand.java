package App.controller.command.impl.management.applicant;

import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.RemoteCommand;
import App.controller.command.exception.CommandException;
import App.model.entity.Applicant;
import App.model.service.ApplicantService;
import App.model.service.exception.ServiceException;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.List;

/**
 * Команда для поиска абитуриентов по фильтрам
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class FindApplicantsCommand extends RemoteCommand {
    private static final ApplicantService service = ApplicantService.getInstance();

    @Override
    public void executeRemote(Param params) throws CommandException, ServiceException{
        //создание фильтра под введённые данные
        StringBuilder sql_filter = new StringBuilder();
        try {
            String surname = (String) params.getParameter(ParamName.SURNAME);
            if (surname != null)
                sql_filter.append(" surname LIKE '").append(surname).append("%'");
            String name = (String) params.getParameter(ParamName.NAME);
            if (name != null)
                sql_filter.append(" AND name LIKE '").append(name).append("%'");
            String patronymic = (String) params.getParameter(ParamName.PATRONYMIC);
            if (patronymic != null)
                sql_filter.append(" AND patronymic LIKE '").append(patronymic).append("%'");

            Integer facultyId = (Integer) params.getParameter(ParamName.FACULTY);
            if (facultyId != null) {
                if (sql_filter.length() > 0) sql_filter.append(" AND");
                sql_filter.append(" faculty_id = ").append(facultyId);
            }

            Boolean isPaid = (Boolean) params.getParameter(ParamName.IS_PAID);
            if (isPaid != null) {
                if (sql_filter.length() > 0) sql_filter.append(" AND");
                sql_filter.append(" on_paid_base = ").append(isPaid ? 1 : 0);
            }

            Integer specId = (Integer) params.getParameter(ParamName.SPECIALIZATION);
            if (specId != null) {
                if (sql_filter.length() > 0) sql_filter.append(" AND");
                sql_filter.append(" id IN ( SELECT applecant_id " +
                                "FROM app_spec WHERE spec_id = ").append(specId)
                        .append(" )");
            }

            List<Applicant> applicants = service.getApplicants(sql_filter.toString());
            EventHandler handler = (EventHandler) params.getParameter(ParamName.RESPONSE);
            Event event = new Event(applicants, Event.NULL_SOURCE_TARGET, Event.ANY);
            handler.handle(event);
        } catch (ClassCastException ex) {
            System.out.println("class cast exception while get params in find applicants command");
            throw new CommandException("class cast exception while get params in find applicants command");
        }
    }
}
