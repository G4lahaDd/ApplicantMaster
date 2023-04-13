package App.model.service;

import App.model.service.exception.NoConnectionException;
import App.model.service.exception.ServiceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private static final String URL = "jdbc:mysql://localhost:3306/applicantdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123321";
    private Connection connection;
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

    public static DBService getInstance(){
        if(INSTANCE == null) INSTANCE = new DBService();
        return INSTANCE;
    }

    public boolean tryReconnect() {
        try {
            this.connection = new ProxyConnection(DriverManager.getConnection(URL, USERNAME, PASSWORD));
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public Connection getConnection() throws NoConnectionException {
        if (connection == null) {
            if(!tryReconnect())
                throw new NoConnectionException();
        }
        return connection;
    }

    public void closeConnection() throws ServiceException {
        if (connection instanceof ProxyConnection) {
            ((ProxyConnection) connection).reallyClose();
        }
    }
}
