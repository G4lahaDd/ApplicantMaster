package App.model.entity.groups;

import App.model.entity.Faculty;

import java.util.ArrayList;
import java.util.List;

public class FacultyThread {
    private Faculty faculty;
    private List<SpecializationThread> specializations = new ArrayList<>();

    public FacultyThread(Faculty faculty){
        this.faculty = faculty;
    }

    public String getAbbreviation(){
        return faculty.getAbbreviation();
    }

    public List<SpecializationThread> getSpecializations() {
        return specializations;
    }
}
