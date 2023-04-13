package App.model.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Faculty {
    private Integer id = -1;
    private String name;
    private StringProperty abbrProperty = new SimpleStringProperty();
    private String abbreviation;
    private Integer groupCode;
    private List<Specialization> specializations;

    public Faculty(){
        name = "";
        abbreviation = "";
        groupCode = 0;
    }

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        abbrProperty.set(abbreviation);
        this.abbreviation = abbreviation;
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<Specialization> specializations){
        this.specializations = specializations;
    }


    public void addSpecializations(Specialization specialization) {
        if(specializations == null){
            specializations = new ArrayList<>();
        }
       this.specializations.add(specialization);
    }

    public void removeSpecializations(Specialization specialization) {
        if(specializations == null){
            return;
        }
        this.specializations.remove(specialization);
    }

    public Integer getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
    }

    public StringProperty getAbbrProperty(){
        return abbrProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) && name.equals(faculty.name) && abbrProperty.equals(faculty.abbrProperty) && abbreviation.equals(faculty.abbreviation) && groupCode.equals(faculty.groupCode) && Objects.equals(specializations, faculty.specializations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, abbrProperty, abbreviation, groupCode, specializations);
    }

    @Override
    public String toString() {
        return "FacultyValidator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", groupCode=" + groupCode +
                '}';
    }
}
