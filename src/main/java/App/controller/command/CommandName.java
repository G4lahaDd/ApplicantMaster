package App.controller.command;

import App.controller.command.impl.*;

public enum CommandName {
    INIT{{
        current = new InitCommand();
    }},
    EXIT{{
        current = new ExitCommand();
    }},
    OPEN_MAIN_WINDOW{{
        current = new OpenMainWindowCommand();
    }},
    LOGIN{{
        current = new LoginCommand();
    }};
    Command current = null;
    public Command getCurrentCommand() {
        return current;
    }
}
