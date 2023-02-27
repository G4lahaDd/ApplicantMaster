package App.controller.command;

import App.controller.command.exception.CommandException;

public interface Command {
    void execute(Param params) throws CommandException;
}
