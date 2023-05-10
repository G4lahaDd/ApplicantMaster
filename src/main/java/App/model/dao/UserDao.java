package App.model.dao;

import App.model.dao.exception.DaoException;
import App.model.entity.User;
import App.model.service.DBService;
import App.model.service.exception.NoConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Класс для связывания сущностей пользователя и базы данных
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class UserDao {
    //region SQL запросы
    private static final String SQL_GET_USER = "SELECT * FROM users WHERE login = ?";
    private static final String SQL_ADD_USER = "INSERT INTO users VALUES (?,?,?)";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE id = ?";
    //endregion

    private final DBService db = DBService.getInstance();

    /**
     * Единственный экземпляр класса
     */
    private static final UserDao INSTANCE = new UserDao();

    private UserDao(){}

    /**
     * Получение единственного экземпляра класса
     */
    public static UserDao getInstance() {
        return INSTANCE;
    }

    /**
     * Получение пользователя по его логину
     * @throws DaoException Ошибка выполнения запроса
     * @throws NoConnectionException Ошибка подключения к сети
     */
    public User getUser(String login) throws DaoException, NoConnectionException {
        try(
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_USER);
                )
        {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setLogin(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setMail(resultSet.getString(4));
                return user;
            }
            else return null;
        }catch (SQLException ex){
            System.out.println("Error while get user: " + ex.getMessage());
            throw new DaoException("Error while get user, login = " + login);
        }
    }
}
