package App.model.entity;

/**
 * Класс, описывающий специальность
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class Specialization {
    private Integer id;
    private String name;
    private String code;
    private Integer groupCode;
    private Integer budgedPlaces;
    private Integer paidPlaces;
    private Subject firstSubject;
    private Subject secondSubject;

    /**
     * Возвращает идентификатор специальности.
     * @return идентификатор специальности
     */
    public Integer getId() {
        return id;
    }
    /**
     * Устанавливает идентификатор специальности.
     * @param id идентификатор специальности
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * Возвращает название специальности.
     * @return название специальности
     */
    public String getName() {
        return name;
    }
    /**
     * Устанавливает название специальности.
     * @param name название специальности
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Возвращает код специальности.
     * @return код специальности
     */
    public String getCode() {
        return code;
    }
    /**
     * Устанавливает код специальности.
     * @param code код специальности
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * Возвращает количество бюджетных мест на специальности.
     * @return количество бюджетных мест на специальности
     */
    public Integer getBudgedPlaces() {
        return budgedPlaces;
    }
    /**
     * Устанавливает количество бюджетных мест на специальности.
     * @param budgedPlaces количество бюджетных мест на специальности
     */
    public void setBudgedPlaces(Integer budgedPlaces) {
        this.budgedPlaces = budgedPlaces;
    }

    /**
     * Возвращает количество мест на платной основе.
     * @return количество мест на платной основе
     */
    public Integer getPaidPlaces() {
        return paidPlaces;
    }
    /**
     * Устанавливает количество мест на платной основе.
     * @param paidPlaces количество мест на платной основе
     */
    public void setPaidPlaces(Integer paidPlaces) {
        this.paidPlaces = paidPlaces;
    }
    /**
     * Возвращает первый предмет для поступления.
     * @return первый предмет для поступления
     */
    public Subject getFirstSubject() {
        return firstSubject;
    }
    /**
     * Устанавливает первый предмет для поступления.
     * @param firstSubject первый предмет для поступления
     */
    public void setFirstSubject(Subject firstSubject) {
        this.firstSubject = firstSubject;
    }
    /**
     * Возвращает второй предмет для поступления.
     * @return второй предмет для поступления
     */
    public Subject getSecondSubject() {
        return secondSubject;
    }
    /**
     * Устанавливает второй предмет для поступления.
     * @param secondSubject второй предмет для поступления
     */
    public void setSecondSubject(Subject secondSubject) {
        this.secondSubject = secondSubject;
    }
    /**
     * Возвращает код группы.
     * @return код группы
     */
    public Integer getGroupCode() {
        return groupCode;
    }
    /**
     * Устанавливает код группы.
     * @param groupCode код группы
     */
    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public String toString() {
        return "Specialization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", groupCode=" + groupCode +
                ", budgedPlaces=" + budgedPlaces +
                ", paidPlaces=" + paidPlaces +
                ", firstSubject=" + firstSubject +
                ", secondSubject=" + secondSubject +
                '}';
    }
}
