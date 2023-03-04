package App.model.service;

import App.model.dao.ApplicantDao;
import App.model.dao.exception.DaoException;
import App.model.entity.Applicant;
import App.model.service.exception.ServiceException;
import App.view.controls.ApplicantsPane;

import java.util.List;

public class ApplicantService {

    private static final ApplicantDao appDao = ApplicantDao.getInstance();
    private static final ApplicantService INSTANCE = new ApplicantService();
    private ApplicantService(){}

    public static ApplicantService getInstance(){
        return INSTANCE;
    }

    public List<Applicant> getApplicants(String filter) throws ServiceException{
        List<Applicant> result;
        try{
        if(filter.isEmpty() || filter == null){
            result = appDao.getAllApplicant();
        }
        else {
            result = appDao.getAllApplicantByFilter(filter);
        }
        }catch(DaoException ex){
            System.out.println("Error while get applicants filter = " + filter + ex.getMessage());
            throw new ServiceException("Error while get applicants filter = " + filter);
        }
        return result;
    }
}
