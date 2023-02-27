package App.controller.command.impl;

import App.controller.command.Command;
import App.controller.command.Param;
import App.controller.command.exception.CommandException;
import App.view.Main;
import App.view.MainScreen;
import App.view.SplashScreen;

public class OpenMainWindowCommand implements Command {
    @Override
    public void execute(Param params) throws CommandException {
        Main.joinLoadThread();
        Main.getCurrentWindow().close();
        new MainScreen();
    }
}
