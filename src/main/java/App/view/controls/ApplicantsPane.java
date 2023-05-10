package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.view.ConfirmBox;
import App.view.MessageBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Класс, описывающий панель для отображения и поиска абитуриентов
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ApplicantsPane extends GridPane implements Initializable, Refreshable {
    //region Компоненты
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnFind;
    @FXML
    private Button btnDeleteSelected;
    @FXML
    private ListView<ApplicantTableRow> applicantsList;
    @FXML
    private ChoiceBox<String> chbFaculty;
    @FXML
    private ChoiceBox<String> chbSpecialization;
    @FXML
    private TextField tfQuery;
    @FXML
    private CheckBox chkIsPaid;
    //endregion
    private static final Controller controller = Controller.getInstance();

    private ObservableList<String> facultiesAbbr;
    private ObservableList<String> specCodes;
    private List<Faculty> faculties;
    private Faculty selectedFaculty;
    private Specialization selectedSpecialization;

    /**
     * Конструктор панели абитуриентов
     * @param OnOpenAddApplicantPage метод обработки события добавления абитуриента
     */
    public ApplicantsPane(EventHandler OnOpenAddApplicantPage) {
        super();
        load();
        btnAdd.setOnAction(OnOpenAddApplicantPage);
    }

    /**
     * Загрузка компонентов интерфейса из ресурсов
     */
    private void load() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicantsPane.fxml"));
        try {
            loader.setController(this);
            Node node = loader.load();
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'ApplicantsPane.fxml' component:\n" + ex.getMessage());
        }
    }

    /**
     * Инициализация графических компонентов панели
     * @param url Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Загрузка факультетов
        Param returnParam = new Param();
        controller.doCommand("load-faculties",returnParam);
        faculties = (List<Faculty>)returnParam.getParameter(ParamName.RETURN);
        applicantsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Заполнение списка аббревиатур факультетов
        facultiesAbbr = FXCollections.observableArrayList();
        if (faculties != null)
            for (Faculty faculty : faculties) {
                facultiesAbbr.add(faculty.getAbbreviation());
            }
        facultiesAbbr.add("Любой");
        chbFaculty.setOnAction(EventHandler -> selectFaculty());
        chbFaculty.setItems(facultiesAbbr);
        chbFaculty.setValue("Любой");

        updateSpecChoiceBox();
        chbSpecialization.setOnAction(EventHandler -> selectSpecialization());

        btnFind.setOnAction(EventHandler -> find());
        btnDeleteSelected.setOnAction(EventHandler -> deleteSelected());

        //Подсказки для поискового поля
        tfQuery.setPromptText("Фамилия Имя Отчество");
        tfQuery.setTooltip(new Tooltip("Для пропуска одного из слов используйте символ '%'" +
                "\nПример: для поиска по имени - \"% Имя\" "));
    }


    @Override
    public void refresh() {
        //ОБновление списка аббревиатур факультетов
        facultiesAbbr = FXCollections.observableArrayList();
        if (faculties != null)
            for (Faculty faculty : faculties) {
                facultiesAbbr.add(faculty.getAbbreviation());
            }
        facultiesAbbr.add("Любой");
        chbFaculty.setItems(facultiesAbbr);
        chbFaculty.setValue("Любой");

        updateSpecChoiceBox();
    }

    /**
     * Выбор факультета
     */
    private void selectFaculty() {
        String abbr = chbFaculty.getValue();
        if (abbr == null) return;
        if (abbr.isEmpty() || abbr.equals("Любой")) { //Отмена выделения текущего факультета
            selectedFaculty = null;
            updateSpecChoiceBox();
        } else { //Выбор факультета
            Optional<Faculty> faculty = faculties.stream()
                    .filter(x -> x.getAbbreviation()
                    .equals(abbr)).findFirst();
            if(faculty.isEmpty()) return;
            selectedFaculty = faculty.get();

            specCodes = FXCollections.observableArrayList();
            specCodes.add("Любая");
            //обновление списка специальностей при выборе факультета
            for (Specialization spec : selectedFaculty.getSpecializations()) {
                specCodes.add(spec.getCode());
            }
            chbSpecialization.setItems(specCodes);
            chbSpecialization.setValue("Любая");
        }
    }

    /**
     * Обновление данных выпадающего списка специальностей
     */
    private void updateSpecChoiceBox() {
        specCodes = FXCollections.observableArrayList();
        specCodes.add("Любая");
        chbSpecialization.setItems(specCodes);
        chbSpecialization.setValue("Любая");
        selectedSpecialization = null;
    }

    /**
     * Выбор специальности
     */
    private void selectSpecialization() {
        String code = chbSpecialization.getValue();
        if(code == null) return;
        if(code.isEmpty() || code.equals("Любая")){
            selectedSpecialization = null;
        }else{
            if(selectedFaculty == null) return;
            //Поиск выбранной специальности
            Optional<Specialization> specialization = selectedFaculty
                    .getSpecializations().stream()
                    .filter(x -> x.getCode().equals(code))
                    .findFirst();
            if(specialization.isEmpty()) return;
            selectedSpecialization = specialization.get();
        }
    }

    /**
     * Поиск абитуриента по введённым фильтрам
     */
    private void find(){
        //Добавление параметров для выполнения команды посиска
        Param param = new Param();
        param.addParameter(ParamName.RESPONSE, (EventHandler)this::getResult);

        if(selectedFaculty != null)
            param.addParameter(ParamName.FACULTY, selectedFaculty.getId());
        if(selectedSpecialization != null)
            param.addParameter(ParamName.SPECIALIZATION, selectedSpecialization.getId());

        String initials = tfQuery.getText();
        if(!initials.isEmpty()){
            String[] query = initials.trim().split(" ");
            if(query.length >= 1)
                param.addParameter(ParamName.SURNAME, query[0]);
            if(query.length >= 2)
                param.addParameter(ParamName.NAME, query[1]);
            if(query.length >= 3)
                param.addParameter(ParamName.PATRONYMIC, query[2]);
        }

        if(chkIsPaid.isSelected()){
            param.addParameter(ParamName.IS_PAID, true);
        }

        Controller.getInstance().doCommand("find-applicants", param);
    }

    /**
     * Получение результатов поиска абитуриентов
     * @param event Объект события, который содержит рещультат
     */
    private void getResult(Event event){
        Object object = event.getSource();
        if(object == null){
            new MessageBox("Абитуриенты не найдены");
            return;
        }
        if(object instanceof List){
            if(((List<?>) object).isEmpty()){
                new MessageBox("Абитуриенты не найдены");
                return;
            }
            List<Applicant> applicants = (List<Applicant>)object;

            ObservableList<ApplicantTableRow> items = applicantsList.getItems();
            if(items == null){
                items = FXCollections.observableArrayList();
                applicantsList.setItems(items);
            }
            items.clear();
            //Заполнение списка абитуриентами
            for (int i = 0; i < applicants.size(); i++){
                ApplicantTableRow row = new ApplicantTableRow(applicants.get(i));
                row.prefWidthProperty().bind(applicantsList.widthProperty().subtract(25));
                items.add(row);
            }
        }
    }

    /**
     * Удаление выбранных абитурентов
     */
    private void deleteSelected(){
        ObservableList<ApplicantTableRow> selection = applicantsList.getSelectionModel().getSelectedItems();
        if(selection.size() == 0) return;
        //Формирование окна подтверждения действия
        String message = "Вы уверены что хотите удалить " + selection.size();
        if(selection.size() == 1) message += " запись?";
        else if(selection.size() < 5) message += " записи?";
        else message += " записей?";
        //Подтверждения действия
        if(!ConfirmBox.Show(message)){
            return;
        }
        //Удаление абитуриентов
        ObservableList<ApplicantTableRow> rows = applicantsList.getItems();
        for(int i = selection.size() - 1; i >= 0; i--){
            selection.get(i).delete();
            rows.remove(selection.get(i));
        }
    }
}
