package App.model.service;

import App.model.service.exception.ServiceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private static final String URL = "jdbc:mysql://localhost:3306/applicantdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123321";
    private Connection connection;
    private static final DBService INSTANCE = new DBService();

    private DBService(){
        try{

            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try{
                this.connection = new ProxyConnection(DriverManager.getConnection(URL, USERNAME, PASSWORD));
            }
            catch (SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
        catch(Exception ex){
            System.out.println("Error while connecting to DB:");
            System.out.println(ex);
        }
    }

    public static DBService getInstance(){
        return INSTANCE;
    }

    public Connection getConnection() throws ServiceException, SQLException {
        if(connection == null) throw new ServiceException("No database connection");
        return connection;
    }

    public void closeConnection() throws ServiceException{
        if(connection instanceof ProxyConnection){
            ((ProxyConnection) connection).reallyClose();
        }
    }
}
