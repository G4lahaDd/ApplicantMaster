package App.controller.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для параметров передаваемых в команду
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class Param {

    /**
     * Коллекция ключ-значение хранящая параметры
     */
    private Map<ParamName, Object> params;

    public Param(){
        params = new HashMap<>();
    }

    /**
     * Получение параметра
     * @param key Ключ идентификатора
     * @return Значение параметра, null если такого параметра нет
     */
    public Object getParameter(ParamName key){
        return params.get(key);
    }

    /**
     * Устанавливает параметр
     * @param key Ключ идентификатора
     * @param value значение параметра
     */
    public void addParameter(ParamName key, Object value){
        params.put(key, value);
    }
}
