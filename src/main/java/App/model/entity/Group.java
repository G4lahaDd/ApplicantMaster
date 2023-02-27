package App.model.entity;

import java.util.List;

public class Group {
    private String code;
    private List<Applicant> applicants;

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
        this.applicants.add(applicant);
    }
}
