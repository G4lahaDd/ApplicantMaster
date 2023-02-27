package App.model.entity;

public class Specialization {
    private Integer id;
    private String name;
    private String code;
    private Integer groupCode;
    private Integer budgedPlaces;
    private Integer paidPlaces;
    private Subject firstSubject;
    private Subject secondSubject;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getBudgedPlaces() {
        return budgedPlaces;
    }

    public void setBudgedPlaces(Integer budgedPlaces) {
        this.budgedPlaces = budgedPlaces;
    }

    public Integer getPaidPlaces() {
        return paidPlaces;
    }

    public void setPaidPlaces(Integer paidPlaces) {
        this.paidPlaces = paidPlaces;
    }

    public Subject getFirstSubject() {
        return firstSubject;
    }

    public void setFirstSubject(Subject firstSubject) {
        this.firstSubject = firstSubject;
    }

    public Subject getSecondSubject() {
        return secondSubject;
    }

    public void setSecondSubject(Subject secondSubject) {
        this.secondSubject = secondSubject;
    }

    public Integer getGroupCode() {
        return groupCode;
    }

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
