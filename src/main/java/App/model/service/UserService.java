package App.model.service;

import App.model.dao.UserDao;
import App.model.dao.exception.DaoException;
import App.model.entity.User;
import App.model.service.exception.ServiceException;

/**
 * Класс для работы с пользователями
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class UserService {
    private static final UserService INSTANCE = new UserService();
    private static  final UserDao dao = UserDao.getInstance();
    private UserService(){}

    /**
     * Возвращает единственный экземпляр класса
     * @return Единственный экземпляр класса
     */
    public static UserService getInstance(){
        return INSTANCE;
    }

    /**
     * Получение пользователя по логину
     * @param login Логин пользователя
     * @return Пользователь,если найден, иначе null
     * @throws ServiceException Ошибка выполнения или сети
     */
    public User getUser(String login) throws ServiceException {
        try{
            return dao.getUser(login);
        } catch (DaoException ex){
            System.out.println("Exception while get user, login = " + login + "\n" + ex.getMessage());
            throw new ServiceException("Exception while get user, login = " + login);
        }
    }
}
