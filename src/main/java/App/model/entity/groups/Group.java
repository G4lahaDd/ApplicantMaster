package App.model.entity.groups;

import App.model.entity.Applicant;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String code;
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

    public void addApplicant(Applicant applicant){
        if(applicants == null){
            applicants = new ArrayList<>();
        }
        this.applicants.add(applicant);
    }
}
