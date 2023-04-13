package App.model.entity.groups;

import App.model.entity.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FacultyThread {
    private Faculty faculty;
    private List<SpecializationThread> specializations;

    public FacultyThread(Faculty faculty){
        specializations = new ArrayList<>();
        this.faculty = faculty;
    }

    public String getAbbreviation(){
        return faculty.getAbbreviation();
    }

    public List<SpecializationThread> getSpecializations() {
        return specializations;
    }

    public void addSpecialization(SpecializationThread thread){
        specializations.add(thread);
    }

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

    public void CreateGroups(){
        for (SpecializationThread specThread : specializations) {
            specThread.CreateGroups(faculty.getGroupCode());
        }
    }
}
