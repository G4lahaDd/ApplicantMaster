package App.model.service.validator;

/**
 * Интерфейс, описывающий прокерку объектов на валидность данных
 */
public interface Validator {
    /**
     * Прокерка объекта на валидность данных
     *
     * @param object объект для проверки
     * @return true если объект валидный, иначе false
     */
    boolean isValid(Object object);
}
