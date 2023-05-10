package App.controller.command;

/**
 * Класс для хранения и передачи изменяемого значения по ссылке
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class Container<T> {
    private T value;

    public Container(){}

    public Container(T value){
        this.value = value;
    }

    public T get(){
        return value;
    }

    public void set(T value){
        this.value = value;
    }

    /**
     * Проверяет является ли контейнер пустым
     * @return true - если пустой, иначе false
     */
    public boolean isEmpty(){
        return value == null;
    }
}
