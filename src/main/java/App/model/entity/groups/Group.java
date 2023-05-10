package App.model.entity.groups;

import App.model.entity.Applicant;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывающий группу студентов
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class Group {

    /**
     * Код группы
     * 1XXYYYZZ, где
     * XX - код факультета
     * YYY - код группы
     * ZZ - год поступления
     */
    private String code;

    /**
     * Студенты относящиеся к группе
     */
    private List<Applicant> applicants;

    public Group(String code){
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Applicant> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Applicant> applicants) {
        this.applicants = applicants;
    }

    /**
     * Добавление студента в группу
     */
    public void addApplicant(Applicant applicant){
        if(applicants == null){
            applicants = new ArrayList<>();
        }
        this.applicants.add(applicant);
    }
}
