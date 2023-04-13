package App.controller.command.impl.general;

import App.controller.command.Command;
import App.controller.command.Container;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.controller.command.exception.CommandException;
import App.model.service.DBService;
import App.view.ReconnectBox;

public class ReconnectCommand implements Command {
    private static final DBService service = DBService.getInstance();

    @Override
    public void execute(Param params) throws CommandException {
        if(params == null) return;
        Object object = params.getParameter(ParamName.BOOL_CONTAINER);
        if(object == null || !(object instanceof Container<?>)) return;
        Container<Boolean> result = (Container<Boolean>)object;
        while(true){
            if(ReconnectBox.Show()){
                if(service.tryReconnect()){
                    result.set(true);
                    break;
                }
            }else{
                result.set(false);
                break;
            }

        }
    }
}
