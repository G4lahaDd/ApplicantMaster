package App.model.dao;

import App.model.dao.exception.DaoException;
import App.model.entity.Applicant;
import App.model.service.DBService;
import App.model.service.exception.NoConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicantDao {
    private static final String SQL_GET_ALL_APPLICANT = "SELECT * FROM applicants";
    private static final String SQL_GET_ALL_APPLICANT_BY_FILTER = "SELECT * FROM applicants WHERE ";
    private static final String SQL_ADD_APPLICANT = "INSERT INTO applicantdb.applicants (name, surname, patronymic, language, first, second, mark, on_paid_base, faculty_id, birthday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_APPLICANT = "UPDATE applicants " +
            "SET name = ?, surname = ?, " +
            "patronymic = ?, language = ?, " +
            "first = ?, second = ?, mark = ?, " +
            "on_paid_base = ?, faculty_id = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_APPLICANT = "DELETE FROM applicants WHERE id = ?";
    private static final String SQL_GET_PRIORITY_LIST = "SELECT * FROM app_spec WHERE applecant_id = ?";
    private static final String SQL_DELETE_PRIORITY_LIST = "DELETE FROM app_spec WHERE applecant_id = ?";
    private static final String SQL_ADD_PRIORITY_LIST = "INSERT INTO app_spec VALUES ";

    private final DBService db = DBService.getInstance();
    private static final ApplicantDao INSTANCE = new ApplicantDao();

    private ApplicantDao(){}
    public static ApplicantDao getInstance(){
        return INSTANCE;
    }

    public List<Applicant> getAllApplicant() throws DaoException, NoConnectionException {
        List<Applicant> results = new ArrayList<>();
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_APPLICANT);
        ) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                results.add(CreateApplicant(resultSet));
            }
        } catch (SQLException ex) {
            throw new DaoException("Error while get all applicants");
        }
        return results;
    }

    public List<Applicant> getAllApplicantByFilter(String sql_filter) throws DaoException, NoConnectionException{
        List<Applicant> results = new ArrayList<>();
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_APPLICANT_BY_FILTER + sql_filter);
        ) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                results.add(CreateApplicant(resultSet));
            }
        }
        catch (SQLException ex) {
            throw new DaoException("Error while get all applicants by filter");
        }
        return results;
    }

    public void addApplicant(Applicant applicant) throws DaoException, NoConnectionException{
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_ADD_APPLICANT, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, applicant.getName());
            statement.setString(2, applicant.getSurname());
            statement.setString(3, applicant.getPatronymic());
            statement.setInt(4,applicant.getLanguagePoints());
            statement.setInt(5,applicant.getFirstSubjPoints());
            statement.setInt(6,applicant.getSecondSubjPoints());
            statement.setInt(7,applicant.getSchoolMark());
            statement.setBoolean(8, applicant.getOnPaidBase());
            statement.setInt(9, applicant.getFacultyId());
            statement.setDate(10,java.sql.Date.valueOf(
                    applicant.getBirthday()));

            int rowsUpdate = statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                applicant.setId(resultSet.getInt(1));
                addPriorityList(applicant.getPrioritySpecializations(), applicant.getId());
            }

        } catch (SQLException ex) {
            throw new DaoException("Error while add applicant \n\t" + ex.getMessage());
        }
    }

    public void updateApplicant(Applicant applicant) throws DaoException, NoConnectionException{
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_APPLICANT);
        ) {
            statement.setString(1, applicant.getName());
            statement.setString(2, applicant.getSurname());
            statement.setString(3, applicant.getPatronymic());
            statement.setInt(4,applicant.getLanguagePoints());
            statement.setInt(5,applicant.getFirstSubjPoints());
            statement.setInt(6,applicant.getSecondSubjPoints());
            statement.setInt(7,applicant.getSchoolMark());
            statement.setBoolean(8, applicant.getOnPaidBase());
            statement.setInt(9, applicant.getFacultyId());
            statement.setDate(10,java.sql.Date.valueOf(
                    applicant.getBirthday()));
            statement.setInt(11,applicant.getId());
            statement.executeUpdate();

            updatePriorityList(applicant.getPrioritySpecializations(),applicant.getId());
        } catch (SQLException ex) {
            throw new DaoException("Error while add applicant");
        }
    }

    public void deleteApplicant(int id) throws DaoException, NoConnectionException{
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_APPLICANT);
        ) {
            statement.setInt(1,id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException("Error while delete applicant");
        }
    }

    private Map<Integer, Integer> getPriorityList(int id) throws DaoException, NoConnectionException{
        Map<Integer, Integer> result = new HashMap<>();
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_PRIORITY_LIST);
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                result.put(resultSet.getInt(2),resultSet.getInt(3));
            }
        } catch (SQLException ex) {
            throw new DaoException("Error while get list of priority specializations");
        }

        return result;
    }

    private void addPriorityList(Map<Integer, Integer> prioritySpec, int id) throws DaoException, NoConnectionException{
        StringBuilder stringBuilder = new StringBuilder(SQL_ADD_PRIORITY_LIST);
        for (Map.Entry<Integer, Integer> entry : prioritySpec.entrySet()) {
           stringBuilder.append("(" + id + ", " + entry.getKey() + ", " + entry.getValue() + "), ");
        }
        int count = stringBuilder.length();
        stringBuilder.delete(count-2, count-1);
        stringBuilder.append(";");

        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(stringBuilder.toString());
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException("Error while add list of priority specializations " + ex.getMessage());
        }
    }

    private void updatePriorityList(Map<Integer, Integer> prioritySpec, int id) throws DaoException, NoConnectionException {
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_PRIORITY_LIST);
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException("Error while add list of priority specializations");
        }
        addPriorityList(prioritySpec, id);
    }

    private Applicant CreateApplicant(ResultSet resultSet) throws SQLException, DaoException, NoConnectionException{
        Applicant applicant = new Applicant();
        applicant.setId(resultSet.getInt(1));
        applicant.setName(resultSet.getString(2));
        applicant.setSurname(resultSet.getString(3));
        applicant.setPatronymic(resultSet.getString(4));
        applicant.setLanguagePoints(resultSet.getInt(5));
        applicant.setFirstSubjPoints(resultSet.getInt(6));
        applicant.setSecondSubjPoints(resultSet.getInt(7));
        applicant.setSchoolMark(resultSet.getInt(8));
        applicant.setOnPaidBase(resultSet.getBoolean(9));
        applicant.setFacultyId(resultSet.getInt(10));
        applicant.setBirthday(resultSet.getDate(11).toLocalDate());//SQL date to local date
        applicant.setPrioritySpecializations(getPriorityList(applicant.getId()));
        return applicant;
    }
}
