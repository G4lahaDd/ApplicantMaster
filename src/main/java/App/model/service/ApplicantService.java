package App.model.service;

import App.model.dao.ApplicantDao;
import App.model.dao.exception.DaoException;
import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.entity.groups.FacultyThread;
import App.model.entity.groups.SpecializationThread;
import App.model.service.exception.ServiceException;
import App.model.service.validator.ApplicantValidator;
import App.model.service.validator.Validator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApplicantService {

    private static final ApplicationDataService appData = ApplicationDataService.getInstance();
    private static final ApplicantDao appDao = ApplicantDao.getInstance();
    private static final Validator appValidator = ApplicantValidator.getInstance();
    private static final ApplicantService INSTANCE = new ApplicantService();

    private List<Applicant> current;
    private List<Applicant> nepostupili;

    private ApplicantService() {
        current = new ArrayList<>();
        nepostupili = new ArrayList<>();
    }

    public static ApplicantService getInstance() {
        return INSTANCE;
    }

    public List<Applicant> getApplicants(String filter) throws ServiceException {
        List<Applicant> result;
        try {
            if (filter.isEmpty() || filter == null) {
                result = appDao.getAllApplicant();
            } else {
                result = appDao.getAllApplicantByFilter(filter);
            }
        } catch (DaoException ex) {
            System.out.println("Error while get applicants filter = " + filter + ex.getMessage());
            throw new ServiceException("Error while get applicants filter = " + filter);
        }
        return result;
    }

    public List<FacultyThread> createGroups() throws ServiceException {
        List<Faculty> faculties = appData.getFaculties();
        List<FacultyThread> result = new ArrayList<>();
        for (Faculty faculty : faculties) {
            FacultyThread facultyThread = fillFacultyThred(faculty);
            facultyThread.CreateGroups();
            result.add(facultyThread);
        }
        System.out.println(nepostupili.size());
        ApplicationDataService.getInstance().setFacultyThreads(result);
        return result;
    }

    private FacultyThread fillFacultyThred(Faculty faculty) throws ServiceException {
        String filter = " faculty_id = " + faculty.getId();
        List<Applicant> applicants = getApplicants(filter);

        current.clear();
        current.addAll(applicants.stream()
                .sorted(Comparator.comparingInt(Applicant::getTotalMark)
                        .reversed()).toList());

        FacultyThread facultyThread = new FacultyThread(faculty);
        for (Specialization spec : faculty.getSpecializations()) {
            facultyThread.addSpecialization(new SpecializationThread(spec));
        }

        Applicant applicant;
        List<SpecializationThread> specializations;
        while(current.size() > 0){
            boolean find = false;
            applicant = current.get(0);
            specializations = facultyThread.GetSpecializationByPriorityList(applicant.getPrioritySpecializations());
            for(int j = 0; j < specializations.size(); j++){
                if(addApplicantToSpecialization(applicant,specializations.get(j),false)){
                    find = true;
                    break;
                }
            }
            if(find) continue;
            if(applicant.getOnPaidBase()){
                for(int j = 0; j < specializations.size(); j++){
                    if(addApplicantToSpecialization(applicant,specializations.get(j),true)){
                        find = true;
                        break;
                    }
                }
            }
            if(!find){
                current.remove(applicant);
                nepostupili.add(applicant);
            }
        }

        return facultyThread;
    }

    public boolean addApplicant(Applicant applicant) throws ServiceException{
        try{
            if(appValidator.isValid(applicant)){
                appDao.addApplicant(applicant);
                return true;
            }
            else{
                System.out.println("Неверные данные абитуриента." + applicant);
                return false;
            }
        }catch (DaoException ex){
            throw new ServiceException("Error while add applicant \n\t" + ex.getMessage());
        }
    }

    public boolean updateApplicant(Applicant applicant) throws ServiceException{
        try{
            if(appValidator.isValid(applicant)){
                appDao.updateApplicant(applicant);
                return true;
            }
            else{
                System.out.println("Неверные данные абитуриента." + applicant);
                return false;
            }
        }catch (DaoException ex){
            throw new ServiceException("Service: Error while update applicant \n\t" + ex.getMessage());
        }
    }

    public void deleteApplicant(Applicant applicant) throws ServiceException{
        try{
            if(applicant.getId() != null && applicant.getId() > 0)
                appDao.deleteApplicant(applicant.getId());
        }catch (DaoException ex){
            throw new ServiceException("Error while delete applicant \n\t" + ex.getMessage());
        }
    }

    public boolean addApplicantToSpecialization(Applicant applicant, SpecializationThread specialization, boolean onPaid) {
        int places;
        if (onPaid) places = specialization.getPaidPlaces();
        else places = specialization.getBudgedPlaces();
        if (places == 0) {
            Applicant last = specialization.getApplicants().get(specialization.getApplicants().size() - 1);
            if (last.getTotalMark() != applicant.getTotalMark()) return false;
            if (applicant.getFirstSubjPoints() > last.getFirstSubjPoints()) {
                specialization.replaceLast(applicant);
                current.remove(applicant);
                current.add(0,last);
                return true;
            }
        } else {
            specialization.addApplicant(applicant, onPaid);
            current.remove(applicant);
            return true;
        }
        return false;
    }
}

