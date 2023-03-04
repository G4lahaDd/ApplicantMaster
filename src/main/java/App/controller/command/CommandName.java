package App.controller.command;

import App.controller.command.impl.*;

public enum CommandName {
    INIT(new InitCommand()),
    EXIT(new ExitCommand()),
    OPEN_MAIN_WINDOW(new OpenMainWindowCommand()),
    LOGIN(new LoginCommand()),
    FIND_APPLICANTS(new FindApplicantsCommand());

    Command current = null;
    CommandName(Command command){
        current = command;
    }
    public Command getCurrentCommand() {
        return current;
    }
}
