package App.model.service;

import App.model.dao.FacultyDao;
import App.model.dao.SpecializationDao;
import App.model.dao.exception.DaoException;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.service.exception.ServiceException;
import App.model.service.validator.FacultyValidator;
import App.model.service.validator.SpecializationValidator;
import App.model.service.validator.Validator;

import java.util.List;

public class FacultyService {
    private static final FacultyService INSTANCE = new FacultyService();
    private static final FacultyDao facultyDao = FacultyDao.getInstance();
    private static final SpecializationDao specializationDao = SpecializationDao.getInstance();
    private static final Validator validator = FacultyValidator.getInstance();
    private static final Validator specializationvValidator = SpecializationValidator.getInstance();

    private FacultyService() {
    }

    public static FacultyService getInstance() {
        return INSTANCE;
    }

    public List<Faculty> getAllFaculties() throws ServiceException {
        try {
            return facultyDao.getAllFaculty();
        } catch (DaoException ex) {
            System.out.println("Service: Error while get all faculties : " + ex.getMessage());
            throw new ServiceException("Service: Error while get all faculties" + ex.getMessage());
        }
    }

    public boolean addFaculty(Faculty faculty) throws ServiceException {
        try {
            if (validator.isValid(faculty)) {
                facultyDao.addFaculty(faculty);
                if (faculty.getId() != null) {
                    for (Specialization spec : faculty.getSpecializations()) {
                        if (specializationvValidator.isValid(spec)) {
                            specializationDao.addSpecialization(spec, faculty.getId());
                        } else {
                            System.out.println("Не удалось добавить специальность: " + spec);
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (DaoException ex) {
            System.out.println("Error while add faculty : " + faculty + "\n\t" + ex.getMessage());
            throw new ServiceException("Error while add faculty" + ex.getMessage());
        }
    }

    public boolean updateFaculty(Faculty faculty) throws ServiceException {
        try {
            if (!validator.isValid(faculty)) return false;
            facultyDao.updateFaculty(faculty);
            for (Specialization spec : faculty.getSpecializations()) {
                if (specializationvValidator.isValid(spec)) {
                    if (specializationDao.hasSpecialization(spec, faculty.getId())) {
                        specializationDao.updateSpecialization(spec);
                    } else {
                        specializationDao.addSpecialization(spec, faculty.getId());
                    }
                } else {
                    System.out.println("Не удалось обновить специальность: " + spec);
                }
            }
            return true;
        } catch (DaoException ex) {
            System.out.println("Error while update faculty : " + faculty);
            throw new ServiceException("Error while update faculty" + ex.getMessage());
        }
    }

    public void deleteFaculty(Faculty faculty) throws ServiceException{
        try {
            if(faculty.getId() != null && faculty.getId() > 0)
                facultyDao.deleteFaculty(faculty.getId());
        } catch (DaoException ex) {
            System.out.println("Error while update faculty : " + faculty);
            throw new ServiceException("Error while update faculty" + ex.getMessage());
        }
    }
}
