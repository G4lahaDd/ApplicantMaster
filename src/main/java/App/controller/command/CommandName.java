package App.controller.command;

import App.controller.command.impl.general.*;
import App.controller.command.impl.management.*;
import App.controller.command.impl.management.Faculty.*;
import App.controller.command.impl.management.applicant.AddApplicantCommand;
import App.controller.command.impl.management.applicant.DeleteApplicantCommand;
import App.controller.command.impl.management.applicant.FindApplicantsCommand;
import App.controller.command.impl.management.applicant.UpdateApplicantCommand;

public enum CommandName {
    //General
    INIT(new InitCommand()),
    EXIT(new ExitCommand()),
    OPEN_MAIN_WINDOW(new OpenMainWindowCommand()),
    LOGIN(new LoginCommand()),
    LOAD_FACULTIES(new LoadFacultiesCommand()),
    SET_CURRENT_WINDOW(new SetCurrentWindowCommand()),
    GET_CURRENT_WINDOW(new GetCurrentWindowCommand()),
    RECONNECT(new ReconnectCommand()),
    //Faculties
    ADD_FACULTY(new AddFacultyCommand()),
    UPDATE_FACULTY(new UpdateFacultyCommand()),
    DELETE_FACULTY(new DeleteFacultyCommand()),
    //Groups
    CREATE_GROUPS(new CreateGroupsCommand()),
    EXPORT(new ExportCommand()),
    //Applicants
    ADD_APPLICANT(new AddApplicantCommand()),
    UPDATE_APPLICANT(new UpdateApplicantCommand()),
    DELETE_APPLICANT(new DeleteApplicantCommand()),
    FIND_APPLICANTS(new FindApplicantsCommand());

    Command current = null;
    CommandName(Command command){
        current = command;
    }
    public Command getCurrentCommand() {
        return current;
    }
}
