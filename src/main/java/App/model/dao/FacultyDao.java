package App.model.dao;

import App.model.dao.exception.DaoException;
import App.model.entity.Faculty;
import App.model.service.DBService;
import App.model.service.exception.NoConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDao {
    private static final String SQL_GET_BY_ID_FACULTY = "SELECT * FROM faculty " +
            "WHERE id = ?";
    private static final String SQL_GET_ALL_FACULTY = "SELECT * FROM faculty";
    private static final String SQL_ADD_FACULTY = "INSERT INTO faculty (name, abbr, group_code) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_FACULTY = "UPDATE faculty " +
            "SET name = ?, abbr = ?, group_code = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_FACULTY = "DELETE FROM faculty WHERE id = ? ";

    private final DBService db = DBService.getInstance();
    private final SpecializationDao specializationDao = SpecializationDao.getInstance();
    private static final FacultyDao INSTANCE = new FacultyDao();

    private FacultyDao(){}

    public Faculty getFacultyById(int id) throws DaoException, NoConnectionException {
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ID_FACULTY);
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createFaculty(resultSet);
            }
        } catch (SQLException ex) {
            throw new DaoException("Error while get faculty by id");
        }
        return null;
    }

    public List<Faculty> getAllFaculty() throws DaoException, NoConnectionException {
        List<Faculty> result = new ArrayList<>();
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_FACULTY);
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(createFaculty(resultSet));
            }
        } catch (SQLException ex) {
            System.out.println("Error while get all faculties: " + ex.getMessage());
            throw new DaoException("Error while get all faculties");
        }
        return result;
    }

    public void addFaculty(Faculty faculty) throws DaoException, NoConnectionException {
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_ADD_FACULTY, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, faculty.getName());
            statement.setString(2, faculty.getAbbreviation());
            statement.setInt(3, faculty.getGroupCode());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                faculty.setId(resultSet.getInt(1));
            }
        } catch (SQLException ex) {
            System.out.println("DAO: Error while add faculty: " + ex.getMessage());
            throw new DaoException("Error while add faculty \n\t" + ex.getMessage());
        }

    }

    public void updateFaculty(Faculty faculty) throws DaoException, NoConnectionException {
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_FACULTY);
        ) {
            statement.setString(1, faculty.getName());
            statement.setString(2, faculty.getAbbreviation());
            statement.setInt(3, faculty.getGroupCode());
            statement.setInt(4, faculty.getId());

        } catch (SQLException ex) {
            System.out.println("Error while update faculty: " + faculty.toString());
            throw new DaoException("Error while update faculty");
        }
    }

    public int deleteFaculty(int id) throws DaoException, NoConnectionException {
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_FACULTY);
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error while delete faculty" + ex.getMessage());
            throw new DaoException("Error while delete faculty");
        }
    }

    private Faculty createFaculty(ResultSet resultSet) throws SQLException, DaoException, NoConnectionException {
        Faculty faculty = new Faculty();
        faculty.setId(resultSet.getInt(1));
        faculty.setName(resultSet.getString(2));
        faculty.setAbbreviation(resultSet.getString(3));
        faculty.setGroupCode(resultSet.getInt(4));
        faculty.setSpecializations(specializationDao.getAllFacultySpecialization(faculty.getId()));
        return faculty;
    }

    public static FacultyDao getInstance(){
        return INSTANCE;
    }
}
