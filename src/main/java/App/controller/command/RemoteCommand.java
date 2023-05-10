package App.controller.command;

import App.controller.command.exception.CommandException;
import App.model.service.exception.NoConnectionException;
import App.model.service.exception.ServiceException;

/**
 * Команда работающая с сетью
 * Обрабатывает ошибки поключения к сети
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public abstract class RemoteCommand implements Command{

    @Override
    public void execute(Param params) throws CommandException {
        try {
            executeRemote(params);
        }catch(NoConnectionException ex){
            handleNoConnectionException(params);
        }
        catch (Exception ex){
            throw new CommandException("unknown exception: " + ex.getMessage());
        }
    }

    /**
     *
     * @param params Параметры команды
     * @throws CommandException Ошибка выполнения команды
     * @throws ServiceException Ошибка подключения к сети или ошибка выполнения в слое модели
     */
    protected abstract void executeRemote(Param params) throws CommandException, ServiceException;

    /**
     * Обработка исключения подключения к сети
     * @param params Параметры команды
     * @throws CommandException Ошибка выполнения команды
     */
    protected void handleNoConnectionException(Param params) throws CommandException{
        Param param = new Param();
        Container<Boolean> result = new Container<>(false);
        param.addParameter(ParamName.BOOL_CONTAINER, result);
        CommandName.RECONNECT.getCurrentCommand().execute(param);
        if(result.isEmpty()) throw new CommandException("ошибка переподключения");
        if(result.get()) this.execute(params);
    }
}
