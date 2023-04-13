package App.model.service;

import App.model.entity.Faculty;
import App.model.entity.User;
import App.model.entity.groups.FacultyThread;
import App.model.service.exception.ServiceException;
import App.view.Window;

import java.util.List;

public class ApplicationDataService {
    private List<Faculty> faculties;
    private List<FacultyThread> facultyThreads;
    private Window currentWindow;
    private Thread loadThread;
    private User user;


    private static final ApplicationDataService INSTANCE = new ApplicationDataService();
    private ApplicationDataService(){}

    public static ApplicationDataService getInstance(){
        return INSTANCE;
    }

    public void InitData() throws ServiceException {
        FacultyService service = FacultyService.getInstance();
        faculties = service.getAllFaculties();
        for(int i = 0; i < faculties.size(); i++){
            System.out.println(faculties.get(i).toString());
            for (int j = 0; j < faculties.get(i).getSpecializations().size(); j++){
                System.out.println(faculties.get(i).getSpecializations().get(j).toString());
            }
        }
    }

    public boolean login(String login, String password) throws ServiceException{
        UserService service = UserService.getInstance();
        User user = service.getUser(login);
        if(user == null) return false;
        if(user.getPassword().equals(password)){
            this.user = user;
            return true;
        }
        else return false;
    }

    public void removeFaculty(Faculty faculty){
        faculties.remove(faculty);
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public List<FacultyThread> getFacultyThreads() {
        return facultyThreads;
    }

    public void setFacultyThreads(List<FacultyThread> facultyThreads) {
        this.facultyThreads = facultyThreads;
    }

    public Faculty getFacultyById(Integer id){
        return faculties.stream().filter(x -> x.getId()
                .equals(id)).findFirst().get();
    }

    public Thread getLoadThread() {
        return loadThread;
    }

    public void setLoadThread(Thread loadThread) {
        this.loadThread = loadThread;
    }

    public Window getCurrentWindow() {
        return currentWindow;
    }

    public void setCurrentWindow(Window currentWindow) {
        this.currentWindow = currentWindow;
    }
}
