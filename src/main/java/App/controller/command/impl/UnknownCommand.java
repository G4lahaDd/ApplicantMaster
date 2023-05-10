package App.controller.command.impl;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.exception.CommandException;

/**
 * Неизвесная команда
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class UnknownCommand implements Command {

    @Override
    public void execute(Param params) throws CommandException {
        System.out.println("Unknown command");
    }
}
