package App.model.service.validator;

import App.model.entity.Applicant;

/**
 * Валидатор данных абитуриента
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ApplicantValidator implements Validator{
    private static final ApplicantValidator INSTANCE = new ApplicantValidator();

    private ApplicantValidator(){}

    public static Validator getInstance(){
        return INSTANCE;
    }

    @Override
    public boolean isValid(Object object) {
        if (object == null) return false;
        if(!(object instanceof Applicant)) return false;
        Applicant applicant = (Applicant) object;
        if(applicant.getName().isEmpty() || applicant.getName() == null) return false;
        return applicant.getName() != null
                && !applicant.getName().isEmpty()
                && applicant.getSurname() != null
                && !applicant.getSurname().isEmpty()
                && applicant.getPatronymic() != null
                && !applicant.getPatronymic().isEmpty()
                && applicant.getBirthday() != null
                && applicant.getLanguagePoints() != null
                && applicant.getLanguagePoints() >= 0
                && applicant.getLanguagePoints() <= 100
                && applicant.getSchoolMark() != null
                && applicant.getSchoolMark() >= 0
                && applicant.getSchoolMark() <= 100
                && applicant.getFirstSubjPoints() != null
                && applicant.getFirstSubjPoints() >= 0
                && applicant.getFirstSubjPoints() <= 100
                && applicant.getSecondSubjPoints() != null
                && applicant.getSecondSubjPoints() >= 0
                && applicant.getSecondSubjPoints() <= 100
                && applicant.getPrioritySpecializations() != null
                && applicant.getPrioritySpecializations().size() > 0;
    }
}
