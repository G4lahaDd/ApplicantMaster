package App.model.dao;

import App.model.dao.exception.DaoException;
import App.model.entity.User;
import App.model.service.DBService;
import App.model.service.exception.ServiceException;

import java.sql.*;

public class UserDao {
    private static final String SQL_GET_USER = "SELECT * FROM users WHERE login = ?";
    private static final String SQL_ADD_USER = "INSERT INTO users VALUES (?,?,?)";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE id = ?";

    private final DBService db = DBService.getInstance();
    private static final UserDao INSTANCE = new UserDao();

    private UserDao(){}

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public User getUser(String login) throws DaoException{
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
        }catch (SQLException | ServiceException ex){
            System.out.println("Error while get user: " + ex.getMessage());
            throw new DaoException("Error while get user, login = " + login);
        }
    }
}
