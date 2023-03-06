package App.model.entity.groups;

import App.model.entity.Specialization;

import java.util.ArrayList;
import java.util.List;

public class SpecializationThread {
    private Specialization specialization;
    private List<Group> groups = new ArrayList<>();

    public SpecializationThread(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return specialization.getName();
    }

    public List<Group> getGroups() {
        return groups;
    }
}
