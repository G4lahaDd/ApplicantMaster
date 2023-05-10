package App.model.service;

import App.model.service.exception.NoConnectionException;
import App.model.service.exception.ServiceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс, отвечающий за доступ к базе данных
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class DBService {
    private static final String URL = "jdbc:mysql://localhost:3306/applicantdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123321";
    private Connection connection;
    /**
     * Единственный  экземпляр класса
     */
    private static DBService INSTANCE;

    private DBService(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            //this.connection = new ProxyConnection(DriverManager.getConnection(URL, USERNAME, PASSWORD));
        }catch (Exception ex) {
            System.out.println("Error while connecting to DB:");
            System.out.println(ex);
        }
    }

    /**
     * Получение единственного экземпляра класса
     * @return Единсвтвенный экземпляр класса
     */
    public static DBService getInstance(){
        if(INSTANCE == null) INSTANCE = new DBService();
        return INSTANCE;
    }

    /**
     * Попытка создать подключение к БД
     * @return true - если успешно, иначе false
     */
    public boolean tryReconnect() {
        try {
            this.connection = new ProxyConnection(DriverManager.getConnection(URL, USERNAME, PASSWORD));
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Получение подключения к БД
     * @return Подключение к базе данных
     * @throws NoConnectionException Ошибка подключения к сети
     */
    public Connection getConnection() throws NoConnectionException {
        if (connection == null) {
            if(!tryReconnect())
                throw new NoConnectionException();
        }
        return connection;
    }

    /**
     * Закрытие подключения к базе данных
     * @throws ServiceException Ошибка закрытия подключения
     */
    public void closeConnection() throws ServiceException {
        if (connection instanceof ProxyConnection) {
            ((ProxyConnection) connection).reallyClose();
        }
    }
}
