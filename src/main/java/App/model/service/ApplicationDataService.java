package App.model.service;

import App.model.entity.Faculty;
import App.model.entity.User;
import App.model.service.exception.ServiceException;
import App.view.Main;
import App.view.MainScreen;
import App.view.controls.SpecializationPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ApplicationDataService {
    private List<Faculty> faculties;
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

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public Faculty getFacultyById(Integer id){
        return faculties.stream().filter(x -> x.getId()
                .equals(id)).findFirst().get();
    }
}
