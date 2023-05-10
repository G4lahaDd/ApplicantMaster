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

/**
 * Класс бизнес логики для факультета
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class FacultyService {
    /**
     * Единственный экземпляр класса
     */
    private static final FacultyService INSTANCE = new FacultyService();
    private static final FacultyDao facultyDao = FacultyDao.getInstance();
    private static final SpecializationDao specializationDao = SpecializationDao.getInstance();
    private static final Validator validator = FacultyValidator.getInstance();
    private static final Validator specializationvValidator = SpecializationValidator.getInstance();

    private FacultyService() {
    }

    /**
     * Возвращает единственный экземпляр класса
     * @return Единственный экземпляр класса
     */
    public static FacultyService getInstance() {
        return INSTANCE;
    }

    /**
     * Возвращает все факультеты
     * @return список факультетов
     * @throws ServiceException ошибка выполнения или ошибка сети
     */
    public List<Faculty> getAllFaculties() throws ServiceException {
        try {
            return facultyDao.getAllFaculty();
        } catch (DaoException ex) {
            System.out.println("Service: Error while get all faculties : " + ex.getMessage());
            throw new ServiceException("Service: Error while get all faculties" + ex.getMessage());
        }
    }

    /**
     * Добавление факультета
     * @param faculty Факультет для добавления
     * @return true - если успешно, иначе false
     * @throws ServiceException ошибка выполнения или ошибка сети
     */
    public boolean addFaculty(Faculty faculty) throws ServiceException {
        try {
            if (validator.isValid(faculty)) {
                facultyDao.addFaculty(faculty);
                if (faculty.getId() != null) {
                    //Добавление всех специальностей
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

    /**
     * Обновление данных факультета
     * @param faculty Факультет для обновления
     * @return true - если успешно, иначе false
     * @throws ServiceException ошибка выполнения или ошибка сети
     */
    public boolean updateFaculty(Faculty faculty) throws ServiceException {
        try {
            if (!validator.isValid(faculty)) return false;
            facultyDao.updateFaculty(faculty);
            List<Specialization> oldSpecializations= specializationDao.getAllFacultySpecialization(faculty.getId());
            //Поиск специальностей которые были удалены
            for(Specialization spec : oldSpecializations){
                if(hasSpecialization(faculty.getSpecializations(), spec) < 0){
                    specializationDao.deleteSpecialization(spec.getId());
                }
            }
            //Поиск новых специальностей
            for (Specialization spec : faculty.getSpecializations()) {
                if (specializationvValidator.isValid(spec)) {
                    if (hasSpecialization(oldSpecializations, spec) >= 0) {
                        specializationDao.updateSpecialization(spec);
                    } else {
                        System.out.println("add specialization: " + spec.getCode());
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

    /**
     * Проверка наличия специальности в коллекции
     * @param list коллекция специальностей
     * @param specialization специальность
     * @return -1 - если не найдено, иначе индекс элемента
     */
    private int hasSpecialization(List<Specialization> list, Specialization specialization){
        if(specialization.getId() == null) return -1;
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).getId() != null &&
                   list.get(i).getId().equals(specialization.getId())){
                return i;
            }
        }
        return -1;
    }

    /**
     * Удаление факультета
     * @param faculty Факультет для удаления
     * @throws ServiceException ошибка выполнения или ошибка сети
     */
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
