package App.model.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс описывающий сущность абитуриента
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
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

    /**
     * Получение уникального идентификатора абитуриента
     */
    public Integer getId() {
        return id;
    }

    /**
     * Получение уникального идентификатора абитуриента
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Получение имени абитуриента
     */
    public String getName() {
        return name;
    }
    /**
     * Установка имени абитуриента
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Получение фамилии абитуриента
     */
    public String getSurname() {
        return surname;
    }
    /**
     * Установка фамилии абитуриента
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    /**
     * Получение отчества абитуриента
     */
    public String getPatronymic() {
        return patronymic;
    }
    /**
     * Установка отчества абитуриента
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    /**
     * Получение количества баллов за язык
     */
    public Integer getLanguagePoints() {
        return languagePoints;
    }
    /**
     * Установка количества баллов за язык
     */
    public void setLanguagePoints(Integer languagePoints) {
        this.languagePoints = languagePoints;
    }
    /**
     * Получение количества баллов за первый предмет
     */
    public Integer getFirstSubjPoints() {
        return firstSubjPoints;
    }
    /**
     * Установка количества баллов за первый предмет
     */
    public void setFirstSubjPoints(Integer firstSubjPoints) {
        this.firstSubjPoints = firstSubjPoints;
    }
    /**
     * Получение количества баллов за второй предмет
     */
    public Integer getSecondSubjPoints() {
        return secondSubjPoints;
    }
    /**
     * Установка количества баллов за второй предмет
     */
    public void setSecondSubjPoints(Integer secondSubjPoints) {
        this.secondSubjPoints = secondSubjPoints;
    }
    /**
     * Получение оценки по школьному предмету
     */
    public Integer getSchoolMark() {
        return schoolMark;
    }
    /**
     * Установка оценки по школьному предмету
     */
    public void setSchoolMark(Integer schoolMark) {
        this.schoolMark = schoolMark;
    }

    /**
     * Получение информации, рассматривает ли абитуриент платное обучение
     */
    public Boolean getOnPaidBase() {
        return onPaidBase;
    }

    /**
     * Установление информации, рассматривает ли абитуриент платное обучение
     */
    public void setOnPaidBase(Boolean onPaidBase) {
        this.onPaidBase = onPaidBase;
    }
    /**
     * Получение идентификатора факультета
     */
    public Integer getFacultyId() {
        return facultyId;
    }
    /**
     *  Установка идентификатора факультета
     */
    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }
    /**
     * Получение приоритетных специальностей абитуриента
     * (приоритет - идентификатор специальности)
     */
    public Map<Integer, Integer> getPrioritySpecializations() {
        if(prioritySpecializations == null)
        {
            prioritySpecializations = new HashMap<>();
        }
        return prioritySpecializations;
    }

    /**
     * Установка приоритетных специальностей абитуриента
     * (приоритет - идентификатор специальности)
     */
    public void setPrioritySpecializations(Map<Integer, Integer> prioritySpecializations) {
        this.prioritySpecializations = prioritySpecializations;
    }

    /**
     * Возвращает дату рождения абитуриента.
     * @return Дата рождения абитуриента
     */
    public LocalDate getBirthday() {
        return birthday;
    }
    /**
     * Устанавливает дату рождения абитуриента.
     * @param birthday Дата рождения абитуриента
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    /**
     * Возвращает сумму баллов абитуриента по всем предметам и школьной оценке.
     * @return Сумма баллов абитуриента
     */
    public int getTotalMark(){
        return languagePoints + firstSubjPoints + secondSubjPoints + schoolMark;
    }
    /**
     *  Возвращает полное имя абитуриента в формате "Фамилия Имя Отчество".
     *  @return Полное имя абитуриента
     */
    public String getInitials(){
        return surname + " " + name + " " + patronymic;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", languagePoints=" + languagePoints +
                ", firstSubjPoints=" + firstSubjPoints +
                ", secondSubjPoints=" + secondSubjPoints +
                ", schoolMark=" + schoolMark +
                ", onPaidBase=" + onPaidBase +
                ", birthday=" + birthday +
                '}';
    }
}
