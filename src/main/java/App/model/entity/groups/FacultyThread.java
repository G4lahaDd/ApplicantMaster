package App.model.entity.groups;

import App.model.entity.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс для потока студентов относящихся к факультету
 * Используется для формирования списков студентов из абитуриентов
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class FacultyThread {
    /**
     * Факультет к которому относится поток
     */
    private Faculty faculty;

    /**
     * Потоки студентов по специальностям
     */
    private List<SpecializationThread> specializations;

    public FacultyThread(Faculty faculty){
        specializations = new ArrayList<>();
        this.faculty = faculty;
    }

    /**
     * Возвращает аббревиатуру факультета
     */
    public String getAbbreviation(){
        return faculty.getAbbreviation();
    }

    /**
     * Возвращает лист потоков по специальностям
     */
    public List<SpecializationThread> getSpecializations() {
        return specializations;
    }

    /**
     * Добавление потока студентов специальности
     */
    public void addSpecialization(SpecializationThread thread){
        specializations.add(thread);
    }

    /**
     * Возвращает лист специальностей по порядку убывания приоритетности студента
     * @param priority приоритетность специальностей студента (приоритет - id специальности)
     */
    public List<SpecializationThread> GetSpecializationByPriorityList(Map<Integer,Integer> priority){
        List<SpecializationThread> result = new ArrayList<>();
        int count = priority.size();
        for(int j = 1; j <= count; j++){
            int id = priority.get(j);
            SpecializationThread specialization = specializations.stream().filter(x -> x.getId() == id)
                    .findFirst().get();
            result.add(specialization);
        }
        return result;
    }

    /**
     * Расределение студентов по группам
     */
    public void CreateGroups(){
        for (SpecializationThread specThread : specializations) {
            specThread.CreateGroups(faculty.getGroupCode());
        }
    }
}
