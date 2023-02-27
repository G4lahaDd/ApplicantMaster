package App.model.service;

import App.model.dao.UserDao;
import App.model.dao.exception.DaoException;
import App.model.entity.User;
import App.model.service.exception.ServiceException;

public class UserService {
    private static final UserService INSTANCE = new UserService();
    private static  final UserDao dao =UserDao.getInstance();
    private UserService(){}

    public static UserService getInstance(){
        return INSTANCE;
    }

    public User getUser(String login) throws ServiceException {
        try{
            return dao.getUser(login);
        } catch (DaoException ex){
            System.out.println("Exception while get user, login = " + login + "\n" + ex.getMessage());
            throw new ServiceException("Exception while get user, login = " + login);
        }
    }
}
