package App.model.service.validator;

import App.model.entity.Specialization;

/**
 * Валидатор данных специальности
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class SpecializationValidator implements Validator {
    private static final SpecializationValidator INSTANCE = new SpecializationValidator();

    private SpecializationValidator(){}

    public static Validator getInstance(){
        return INSTANCE;
    }
    @Override
    public boolean isValid(Object object) {
        if (object == null) return false;
        if (!(object instanceof Specialization)) return false;
        Specialization spec = (Specialization) object;
        return spec.getName() != null
                && !spec.getName().isEmpty()
                && spec.getCode() != null
                && !spec.getCode().isEmpty()
                && spec.getGroupCode() != null
                && spec.getBudgedPlaces() != null
                && spec.getPaidPlaces() != null
                && spec.getFirstSubject() != null
                && spec.getSecondSubject() != null;
    }
}
