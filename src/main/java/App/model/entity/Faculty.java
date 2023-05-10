package App.model.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс, описывающий сущность факультета
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
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

    /**
     * Получение уникального идентификатора факультета.
     * @return Уникальный идентификатор факультета.
     */
    public Integer getId() {
        return id;
    }
    /**
     *  Установка уникального идентификатора факультета.
     *  @param id Уникальный идентификатор факультета.
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * Получение названия факультета.
     * @return Название факультета.
     */
    public String getName() {
        return name;
    }
    /**
     * Установка названия факультета.
     * @param name Название факультета.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Получение аббревиатуры факультета.
     * @return Аббревиатура факультета.
     */
    public String getAbbreviation() {
        return abbreviation;
    }
    /**
     * Установка аббревиатуры факультета.
     * @param abbreviation Аббревиатура факультета.
     */
    public void setAbbreviation(String abbreviation) {
        abbrProperty.set(abbreviation);
        this.abbreviation = abbreviation;
    }
    /**
     * Получение списка специальностей факультета.
     * @return Список специальностей факультета.
     */
    public List<Specialization> getSpecializations() {
        return specializations;
    }
    /**
     * Установка списка специальностей факультета.
     * @param specializations Список специальностей факультета.
     */
    public void setSpecializations(List<Specialization> specializations){
        this.specializations = specializations;
    }
    /**
     * Добавление специальности в список специальностей факультета.
     * @param specialization Специальность для добавления.
     */
    public void addSpecializations(Specialization specialization) {
        if(specializations == null){
            specializations = new ArrayList<>();
        }
        this.specializations.add(specialization);
    }

    /**
     * Удаление специальности из списка специальностей факультета.
     * @param specialization Специальность для удаления.
     */
    public void removeSpecializations(Specialization specialization) {
        if(specializations == null){
            return;
        }
        this.specializations.remove(specialization);
    }

    /**Возвращает код группы факультета.
     * @return код группы факультета
     */
    public Integer getGroupCode() {
        return groupCode;
    }
    /**
     * Устанавливает код группы факультета.
     * @param groupCode код группы факультета
     */
    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
    }
    /**
     * Возвращает свойство аббревиатуры факультета.
     * @return свойство аббревиатуры факультета
     */
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
