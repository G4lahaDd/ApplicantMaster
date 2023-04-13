package App.model.entity.groups;

import App.model.entity.Applicant;
import App.model.entity.Specialization;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SpecializationThread {
    private Specialization specialization;
    private List<Group> groups;

    private List<Applicant> applicants;
    private int budgedPlaces;
    private int paidPlaces;

    public SpecializationThread(Specialization specialization) {
        groups = new ArrayList<>();
        applicants = new ArrayList<>();
        paidPlaces = specialization.getPaidPlaces();
        budgedPlaces = specialization.getBudgedPlaces();
        this.specialization = specialization;
    }

    public int getId(){
        return specialization.getId();
    }

    public String getName() {
        return specialization.getName();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Applicant> getApplicants() {
        return applicants;
    }

    public int getBudgedPlaces() {
        return budgedPlaces;
    }

    public int getPaidPlaces() {
        return paidPlaces;
    }

    public void addApplicant(Applicant applicant, boolean onPaid){
        applicants.add(applicant);
        if(onPaid) paidPlaces--;
        else budgedPlaces--;
    }

    public void replaceLast(Applicant applicant){
        applicants.remove(applicants.size()-1);
        applicants.add(applicant);
    }

    public void CreateGroups(int facultyCode){
        int groupsCount = (int)(applicants.size() / 30) + 1;
        int groupCodes = 100 + facultyCode;
        groupCodes *= 100000;
        groupCodes += LocalDate.now().getYear() - 2000;
        for (int i = 0; i < groupsCount; i++) {
            int groupCode = groupCodes + (specialization.getGroupCode() + i)*100;
            groups.add(new Group(Integer.toString(groupCode)));
        }

        if(groupsCount == 1){
            groups.get(0).setApplicants(applicants);
            return;
        }

        for (int i = 0; i < applicants.size(); i++) {
            groups.get(i % groupsCount).addApplicant(applicants.get(i));
        }
    }
}
