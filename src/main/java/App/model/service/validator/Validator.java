package App.model.service.validator;

/**
 * The interface Validator.
 */
public interface Validator {
    /**
     * Is object valid.
     *
     * @param object the object
     * @return the true if object is valid and false if not
     */
    boolean isValid(Object object);
}
