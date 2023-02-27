package App.model.service;

import App.model.dao.FacultyDao;
import App.model.dao.exception.DaoException;
import App.model.entity.Faculty;
import App.model.service.exception.ServiceException;

import java.util.List;

public class FacultyService {
    private static final FacultyService INSTANCE = new FacultyService();
    private static final FacultyDao facultyDao = FacultyDao.getInstance();
    private FacultyService(){}

    public static FacultyService getInstance() {
        return INSTANCE;
    }

    public List<Faculty> getAllFaculties() throws ServiceException{
        try{
            return facultyDao.getAllFaculty();
        }
        catch (DaoException ex){
            System.out.println("Error while get all faculties : " + ex.getMessage());
            throw new ServiceException("Error while get all faculties");
        }

    }
}
