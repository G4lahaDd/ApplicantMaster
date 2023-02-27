package App.model.entity;

import java.time.LocalDate;
import java.util.*;

public class Applicant {
    private Integer id;
    private String name;
    private String surname;
    private String patronymic;
    private Integer languagePoints;
    private Integer firstSubjPoints;
    private Integer secondSubjPoints;
    private Integer schoolMark;
    private Boolean onPaidBase;
    private Integer facultyId;
    private LocalDate birthday;
    private Map<Integer, Integer> prioritySpecializations;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Integer getLanguagePoints() {
        return languagePoints;
    }

    public void setLanguagePoints(Integer languagePoints) {
        this.languagePoints = languagePoints;
    }

    public Integer getFirstSubjPoints() {
        return firstSubjPoints;
    }

    public void setFirstSubjPoints(Integer firstSubjPoints) {
        this.firstSubjPoints = firstSubjPoints;
    }

    public Integer getSecondSubjPoints() {
        return secondSubjPoints;
    }

    public void setSecondSubjPoints(Integer secondSubjPoints) {
        this.secondSubjPoints = secondSubjPoints;
    }

    public Integer getSchoolMark() {
        return schoolMark;
    }

    public void setSchoolMark(Integer schoolMark) {
        this.schoolMark = schoolMark;
    }

    public Boolean getOnPaidBase() {
        return onPaidBase;
    }

    public void setOnPaidBase(Boolean onPaidBase) {
        this.onPaidBase = onPaidBase;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Map<Integer, Integer> getPrioritySpecializations() {
        if(prioritySpecializations == null)
        {
            prioritySpecializations = new HashMap<>();
        }
        return prioritySpecializations;
    }

    public void setPrioritySpecializations(Map<Integer, Integer> prioritySpecializations) {
        this.prioritySpecializations = prioritySpecializations;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getTotalMark(){
        return languagePoints + firstSubjPoints + secondSubjPoints + schoolMark;
    }
}
