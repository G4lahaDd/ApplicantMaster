package App.model.entity.groups;

import App.model.entity.Applicant;
import App.model.entity.Specialization;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывающий поток студентов специальности
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class SpecializationThread {
    /**
     * Специальность к которой относится поток
     */
    private Specialization specialization;
    /**
     * группы специальности
     */
    private List<Group> groups;
    /**
     * Студенты относящиеся к потоку
     */
    private List<Applicant> applicants;
    /**
     * Кол-во бюджетных мест на специальности
     */
    private int budgedPlaces;
    /**
     * кол-во платных мест на специальности
     */
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

    /**
     * Добавление студента к потоку
     * @param applicant
     * @param onPaid
     */
    public void addApplicant(Applicant applicant, boolean onPaid){
        applicants.add(applicant);
        if(onPaid) paidPlaces--;
        else budgedPlaces--;
    }

    /**
     * замещение последнего добавленного студента
     * @param applicant новый студент
     */
    public void replaceLast(Applicant applicant){
        applicants.remove(applicants.size()-1);
        applicants.add(applicant);
    }

    /**
     * Распределение студентов на группы
     * @param facultyCode код факультета
     */
    public void CreateGroups(int facultyCode){
        // подсчёт количества групп
        int groupsCount = 1;
        if(applicants.size() > 30)
            groupsCount = (int)(applicants.size() / 30) + 1;
        // Расчёт кода групп
        int groupCodes = 100 + facultyCode;
        groupCodes *= 100000;
        groupCodes += LocalDate.now().getYear() - 2000;
        for (int i = 0; i < groupsCount; i++) {
            int groupCode = groupCodes + (specialization.getGroupCode() + i)*100;
            groups.add(new Group(Integer.toString(groupCode)));
        }
        //Добавление студентов в группы
        if(groupsCount == 1){
            groups.get(0).setApplicants(applicants);
            return;
        }
        for (int i = 0; i < applicants.size(); i++) {
            groups.get(i % groupsCount).addApplicant(applicants.get(i));
        }
    }
}
