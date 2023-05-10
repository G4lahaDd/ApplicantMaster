package App.model.service.validator;

import App.model.entity.Faculty;

/**
 * Валидатор данных факультета
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class FacultyValidator implements Validator {
    private static final FacultyValidator INSTANCE = new FacultyValidator();

    private FacultyValidator(){}

    public static Validator getInstance(){
        return INSTANCE;
    }
    @Override
    public boolean isValid(Object object) {
        if (object == null) return false;
        if (!(object instanceof Faculty)) return false;
        Faculty faculty = (Faculty) object;
        return faculty.getName() != null
                && !faculty.getName().isEmpty()
                && faculty.getAbbreviation() != null
                && !faculty.getAbbreviation().isEmpty()
                && faculty.getGroupCode() != null
                && faculty.getSpecializations() != null;
    }
}
