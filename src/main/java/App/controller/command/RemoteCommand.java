package App.controller.command;

import App.controller.command.exception.CommandException;
import App.model.service.exception.NoConnectionException;
import App.model.service.exception.ServiceException;

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

    protected abstract void executeRemote(Param params) throws CommandException, ServiceException;

    protected void handleNoConnectionException(Param params) throws CommandException{
        Param param = new Param();
        Container<Boolean> result = new Container<>(false);
        param.addParameter(ParamName.BOOL_CONTAINER, result);
        CommandName.RECONNECT.getCurrentCommand().execute(param);
        if(result.isEmpty()) throw new CommandException("ошибка переподключения");
        if(result.get()) this.execute(params);
    }
}
