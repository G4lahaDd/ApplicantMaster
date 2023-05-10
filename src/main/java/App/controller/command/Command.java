package App.controller.command;

import App.controller.command.exception.CommandException;

/**
 * Команда выполняющая определённое действие
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public interface Command {

    /**
     * Выполнение команды с входными параметрами
     *
     * @param params входные параметры
     * @throws CommandException ошибка выполнения команды
     */
    void execute(Param params) throws CommandException;
}
