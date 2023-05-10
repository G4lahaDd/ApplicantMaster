package App.model.service;

import App.model.entity.Faculty;
import App.model.entity.User;
import App.model.entity.groups.FacultyThread;
import App.model.service.exception.ServiceException;
import App.view.Window;

import java.util.List;

/**
 * Класс, отвечающий за хранение информации приложения
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ApplicationDataService {
    private List<Faculty> faculties;
    private List<FacultyThread> facultyThreads;
    private Window currentWindow;
    private Window mainWindow;
    private Thread loadThread;
    private User user;

    /**
     * Единственный экземпляр класса
     */
    private static final ApplicationDataService INSTANCE = new ApplicationDataService();

    private ApplicationDataService() {
    }

    /**
     * Возвращает единственный экземпляр класса
     *
     * @return Единственный экземпляр класса
     */
    public static ApplicationDataService getInstance() {
        return INSTANCE;
    }

    /**
     * Инициализирует данные программы
     *
     * @throws ServiceException Ошибка выполнения или ошибка сети
     */
    public void InitData() throws ServiceException {
        FacultyService service = FacultyService.getInstance();
        faculties = service.getAllFaculties();
        for (int i = 0; i < faculties.size(); i++) {
            System.out.println(faculties.get(i).toString());
            for (int j = 0; j < faculties.get(i).getSpecializations().size(); j++) {
                System.out.println(faculties.get(i).getSpecializations().get(j).toString());
            }
        }
    }

    /**
     * Логинация пользователя
     *
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return true если логинация успешна, иначе false
     * @throws ServiceException Ошибка выполнения или ошибка сети
     */
    public boolean login(String login, String password) throws ServiceException {
        UserService service = UserService.getInstance();
        User user = service.getUser(login);
        if (user == null) return false;
        if (user.getPassword().equals(password)) {
            this.user = user;
            return true;
        } else return false;
    }

    /**
     * Удаление факультета
     * @param faculty факультет
     */
    public void removeFaculty(Faculty faculty){
        faculties.remove(faculty);
    }

    /**
     * Получение списка факультетов
     * @return список факультетов
     */
    public List<Faculty> getFaculties() {
        return faculties;
    }

    /**
     * Получение списка потоков факультета
     * @return список потоков факультета
     */
    public List<FacultyThread> getFacultyThreads() {
        return facultyThreads;
    }

    /**
     * Установление списка потоков факультета
     * @param facultyThreads Список потоков факультета
     */
    public void setFacultyThreads(List<FacultyThread> facultyThreads) {
        this.facultyThreads = facultyThreads;
    }

    /**
     * Получение факультета по индентификатору
     * @param id Уникальный идентификатор факультета
     * @return Факультет
     */
    public Faculty getFacultyById(Integer id){
        return faculties.stream().filter(x -> x.getId()
                .equals(id)).findFirst().get();
    }

    /**
     * Получение потока фоновой загрузки данных
     * @return Поток фоновой загрузки данных
     */
    public Thread getLoadThread() {
        return loadThread;
    }

    /**
     * Установление потока фоновой загрузки данных
     * @param loadThread Поток фоновой загрузки данных
     */
    public void setLoadThread(Thread loadThread) {
        this.loadThread = loadThread;
    }

    /**
     * Получение текущего окна
     * @return Текущее окно
     */
    public Window getCurrentWindow() {
        return currentWindow;
    }

    /**
     * Утановление текущего окна
     * @param currentWindow Текущее окно
     */
    public void setCurrentWindow(Window currentWindow) {
        this.currentWindow = currentWindow;
    }

    /**
     * Получение главного окна программы
     * @return Главное окно программы
     */
    public Window getMainWindow() {
        return mainWindow;
    }

    /**
     * Установление главного окна программы
     * @param mainWindow Главное окно
     */
    public void setMainWindow(Window mainWindow) {
        this.mainWindow = mainWindow;
    }
}
