package App.controller.command;

import java.util.HashMap;
import java.util.Map;

public class Param {
    private Map<ParamName, Object> params;

    public Param(){
        params = new HashMap<>();
    }

    public Object getParameter(ParamName key){
        return params.get(key);
    }

    public void addParameter(ParamName key, Object value){
        params.put(key, value);
    }
}
