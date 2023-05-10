package App.model.dao;

import App.model.dao.exception.DaoException;
import App.model.entity.Specialization;
import App.model.entity.Subject;
import App.model.service.DBService;
import App.model.service.exception.NoConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для связывания сущностей специальности и базы данных
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class SpecializationDao {
    //region SQL запросы
    private static final String SQL_GET_ALL_FACULTY_SPECIALIZATIONS =
            "Select * " +
                    "FROM specializations WHERE faculty_id = ?";
    private static final String SQL_ADD_SPECIALIZATION =
            "INSERT INTO specializations (faculty_id, name, code, budged_places, paid_places, first_subj, second_subj, group_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_HAS_SPECIALIZATION =
            "SELECT * FROM specializations " +
                    "WHERE faculty_id = ? AND code = ?";
    private static final String SQL_UPDATE_SPECIALIZATION = "UPDATE specializations " +
            "SET name = ?, code = ?, " +
            "budged_places = ?, paid_places = ?, " +
            "first_subj = ?, second_subj = ?, group_code = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_SPECIALIZATION =
            "DELETE FROM specializations WHERE id = ?";

    //endregion

    /**
     * Единственный экземпляр класса
     */
    private static final SpecializationDao INSTANCE = new SpecializationDao();
    private final DBService db = DBService.getInstance();

    /**
     * Получение всех специальностей факультета
     * @param facultyId id факультета
     * @throws DaoException Ошибка выполнения запроса
     * @throws NoConnectionException Ошибка подключения к сети
     */
    public List<Specialization> getAllFacultySpecialization(int facultyId) throws DaoException, NoConnectionException {
        List<Specialization> specializations = new ArrayList<>();
        try(
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_FACULTY_SPECIALIZATIONS);
        ){
            statement.setInt(1,facultyId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                specializations.add(createSpecialization(resultSet));
            }
        }catch(SQLException ex){
            throw new DaoException("DAO: Error while get faculty specializations " + ex.getMessage());
        }
        return specializations;
    }

    /**
     * Добавление специальности в базу данных
     *
     * @param specialization специальность
     * @param facultyId id факультета
     * @return кол-во здобавленных строк в БД
     * @throws DaoException Ошибка выполнения запроса
     * @throws NoConnectionException Ошибка подключения к сети
     */
    public int addSpecialization(Specialization specialization, int facultyId) throws DaoException, NoConnectionException{
        try(
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_ADD_SPECIALIZATION,
                        Statement.RETURN_GENERATED_KEYS);
        ){
            statement.setInt(1,facultyId);
            statement.setString(2,specialization.getName());
            statement.setString(3,specialization.getCode());
            statement.setInt(4,specialization.getBudgedPlaces());
            statement.setInt(5,specialization.getPaidPlaces());
            statement.setInt(6,specialization.getFirstSubject().getCode());
            statement.setInt(7,specialization.getSecondSubject().getCode());
            statement.setInt(8,specialization.getGroupCode());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                specialization.setId(resultSet.getInt(1));
                return 1;
            }
        }catch(SQLException ex){
            System.out.println("Error while add specialization: " + specialization.toString());
            throw new DaoException("Error while add specialization: " + specialization.toString());
        }
        return -1;
    }

    /**
     * Проверка наличия специальности у факультета
     * @param specialization специальность
     * @param facultyId факультет
     * @return true - если имеется, иначе false
     * @throws DaoException Ошибка выполнения запроса
     * @throws NoConnectionException Ошибка подключения к сети
     */
    public boolean hasSpecialization(Specialization specialization, int facultyId) throws DaoException, NoConnectionException{
        try(
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_HAS_SPECIALIZATION);
        ){
            statement.setInt(1,facultyId);
            statement.setString(2,specialization.getCode());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        }catch(SQLException ex){
            System.out.println("Error while check specialization: " + ex.getMessage());
            throw new DaoException("Error while check specialization: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Обновление данных специальности
     * @param specialization специальность
     * @return кол-во изменнёных строк в БД
     * @throws DaoException Ошибка выполнения запроса
     * @throws NoConnectionException Ошибка подключения к сети
     */
    public int updateSpecialization(Specialization specialization) throws DaoException, NoConnectionException{
        try(
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_SPECIALIZATION);
        ){
            statement.setString(1,specialization.getName());
            statement.setString(2,specialization.getCode());
            statement.setInt(3,specialization.getBudgedPlaces());
            statement.setInt(4,specialization.getPaidPlaces());
            statement.setInt(5,specialization.getFirstSubject().getCode());
            statement.setInt(6,specialization.getSecondSubject().getCode());
            statement.setInt(7,specialization.getGroupCode());
            statement.setInt(8,specialization.getId());
            return statement.executeUpdate();
        }catch(SQLException ex){
            System.out.println("Error while add specialization: " + specialization.toString());
            throw new DaoException("Error while add specialization: " + specialization.toString());
        }
    }

    /**
     * Удаление специальности
     * @param id id специальности
     * @return кол-во удалённых строк
     * @throws DaoException Ошибка выполнения запроса
     * @throws NoConnectionException Ошибка подключения к сети
     */
    public int deleteSpecialization(int id) throws DaoException, NoConnectionException{
        try(
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_SPECIALIZATION);
        ){
            statement.setInt(1, id);
            return statement.executeUpdate();
        }catch(SQLException ex){
            System.out.println("Error while delete specialization" + ex.getMessage());
            throw new DaoException("Error while delete specialization");
        }
    }

    /**
     * Получение единственно экземпляра класса
     */
    public static SpecializationDao getInstance(){
        return INSTANCE;
    }

    /**
     * Создание объекта специальности из результатов запроса к бд
     * @param resultSet результат запроса
     * @return Специальность
     */
    private Specialization createSpecialization(ResultSet resultSet) throws SQLException{
        Specialization spec = new Specialization();
        spec.setId(resultSet.getInt(1));
        spec.setName(resultSet.getString(3));
        spec.setCode(resultSet.getString(4));
        spec.setBudgedPlaces(resultSet.getInt(5));
        spec.setPaidPlaces(resultSet.getInt(6));
        spec.setFirstSubject(Subject.valueOf(resultSet.getInt(7)));
        spec.setSecondSubject(Subject.valueOf(resultSet.getInt(8)));
        spec.setGroupCode(resultSet.getInt(9));
        return spec;
    }
}
