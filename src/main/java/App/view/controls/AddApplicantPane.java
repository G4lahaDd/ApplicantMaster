package App.view.controls;

import App.controller.Controller;
import App.controller.command.Param;
import App.controller.command.ParamName;
import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.entity.Subject;
import App.view.exception.EmptyFieldException;
import App.view.MessageBox;
import App.view.Parser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Класс, описывающий панель для добавления абитуриентов
 *
 * @author Kazyro I. A.
 * @version 1.0
 */
public class AddApplicantPane extends GridPane implements Initializable, Refreshable {
    //region Компоненты
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfSurname;
    @FXML
    private TextField tfPatronymic;
    @FXML
    private TextField tfLangPoints;
    @FXML
    private TextField tfSchoolPoints;
    @FXML
    private TextField tfFirstPoints;
    @FXML
    private TextField tfSecondPoints;
    @FXML
    private Label lblFirstSubject;
    @FXML
    private Label lblSecondSubject;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ScrollPane scrSpecialization;
    @FXML
    private VBox specializationList;
    @FXML
    private ChoiceBox<String> chbFaculty;
    @FXML
    private CheckBox chkIsPaidCheckBox;
    @FXML
    private GridPane pnlPoints;
    @FXML
    private Button btnAdd;
    //endregion
    private static final Controller controller = Controller.getInstance();
    private List<Faculty> faculties;
    private Faculty selectedFaculty;
    private Applicant applicant;
    private Subject selectedFirstSubj;
    private Subject selectedSecondSubj;
    private PaneMode paneMode;
    private EventHandler onClose;

    /**
     * Стандартный конструктор класса, используется при создании пустой панели
     * без предварительных данных
     */
    public AddApplicantPane() {
        super();
        load();
        loadData();
        paneMode = PaneMode.ADD;
    }

    /**
     * Конструктор класса, используется при создании панели
     * с предварительными данными об изменяемом абитуриенте
     */
    public AddApplicantPane(Applicant applicant) {
        super();
        load();
        this.applicant = applicant;
        loadData();
        paneMode = PaneMode.EDIT;
        SpecializationToggle.clear();
        updateDataOnWindow();
    }

    /**
     * Загрузка данных необходимых для работы панели
     */
    private void loadData() {
        //Загрузка существующих факультетов
        Param returnParam = new Param();
        controller.doCommand("load-faculties",returnParam);
        faculties = (List<Faculty>)returnParam.getParameter(ParamName.RETURN);
        //Заполнение списка аббревиатур факультетов
        ObservableList<String> facultiesAbbr = FXCollections.observableArrayList();
        for (Faculty faculty : faculties) {
            facultiesAbbr.add(faculty.getAbbreviation());
        }
        chbFaculty.setItems(facultiesAbbr);
        chbFaculty.setOnAction(EventHandler -> selectFaculty());
    }

    /**
     * Загрузка компонентов интерфейса из ресурсов
     */
    private void load() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("AddApplicantPane.fxml"));
        try {
            loader.setController(this);
            Node node = loader.load();
            this.getChildren().add(node);
        } catch (IOException ex) {
            System.out.println("Failed to load 'AddApplicantPane.fxml' component:\n" + ex.getMessage());
        }
    }

    /**
     * Инициализация графических компонентов панели
     * @param url Путь к ресурсу с компонентами
     * @param resourceBundle Набор данных необходимых для компонента
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pnlPoints.setVisible(false);
        btnAdd.setOnAction(EventHandler -> add());
        specializationList.prefWidthProperty().bind(scrSpecialization.widthProperty());
        specializationList.setPadding(new Insets(0, 18, 0, 0));
        SpecializationToggle.setOnChooseFirst(this::selectFirstSpecialization);
        SpecializationToggle.setOnLostFirst(EventHandler -> onLostSpecialization());

        //Перегрузка конвертера с обработкой исключения, стандартный имеет необрабатываемое исключение
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            private final StringConverter<LocalDate> base = datePicker.getConverter();

            @Override
            public String toString(LocalDate date) {
                return base.toString(date);
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return base.fromString(string);
                } catch (DateTimeParseException ex) {
                    System.out.println("Неверно введена дата");
                    new MessageBox("Неверно введена дата");
                    return LocalDate.now().minusYears(17);
                }
            }
        };
        datePicker.setConverter(converter);
    }

    @Override
    public void refresh() {
        ObservableList<String> facultiesAbbr = chbFaculty.getItems();
        facultiesAbbr.clear();
        selectedFaculty = null;
        chbFaculty.setValue("");
        specializationList.getChildren().clear();
        //Обновление переключателя
        SpecializationToggle.clear();
        SpecializationToggle.setOnChooseFirst(this::selectFirstSpecialization);
        SpecializationToggle.setOnLostFirst(EventHandler -> onLostSpecialization());
        //Обновление данных факультетов
        for (Faculty faculty : faculties) {
            facultiesAbbr.add(faculty.getAbbreviation());
        }
    }

    /**
     * Установка метода обработки события закрытия панели
     */
    public void setOnClose(EventHandler onClose) {
        this.onClose = onClose;
    }

    /**
     * Выбор специальности
     */
    private void selectFaculty() {
        pnlPoints.setVisible(false);
        String selAbbr = chbFaculty.getValue();
        if (selAbbr == null) return;
        if (selAbbr.isEmpty()) return;
        selectedFaculty = faculties.stream().filter(x -> x.getAbbreviation().equals(selAbbr))
                .findAny().get();
        //Очистка спика специальностей
        specializationList.getChildren().clear();
        SpecializationToggle.clear();
        if (selectedFaculty.getSpecializations() == null) return;
        //Заполнение списка специальностей
        for (Specialization specialization : selectedFaculty.getSpecializations()) {
            specializationList.getChildren().add(new SpecializationToggle(specialization));
        }
    }

    /**
     * Выбор первой специальности, по предметам первой специальности фильтруются остальные специальности
     * @param event Объект события, содержит выбранный переключатель
     */
    private void selectFirstSpecialization(Event event) {
        Object source = event.getSource();
        if (!(source instanceof SpecializationToggle)) return;
        SpecializationToggle toggle = (SpecializationToggle) source;
        Specialization specialization = toggle.getSpecialization();
        selectedFirstSubj = specialization.getFirstSubject();
        selectedSecondSubj = specialization.getSecondSubject();

        //Фильтрация по первому и второму предмету
        for (int i = specializationList.getChildren().size() - 1; i >= 0; i--) {
            SpecializationToggle specToggle = (SpecializationToggle) specializationList.getChildren().get(i);
            if (specToggle.getSpecialization().getFirstSubject() != selectedFirstSubj
                    || specToggle.getSpecialization().getSecondSubject() != selectedSecondSubj) {
                specializationList.getChildren().remove(specToggle);
            }
        }

        //Обновление компонентов ввода
        lblFirstSubject.setText(selectedFirstSubj.toString());
        lblSecondSubject.setText(selectedSecondSubj.toString());
        pnlPoints.setVisible(true);
    }

    /**
     * При отмене выбора последней специальности сбрасывается фильтр специальностей
     */
    private void onLostSpecialization() {
        pnlPoints.setVisible(false);
        specializationList.getChildren().clear();
        for (Specialization specialization : selectedFaculty.getSpecializations()) {
            specializationList.getChildren().add(new SpecializationToggle(specialization));
        }
    }

    /**
     * Обновление данных абитуриента
     */
    private void updateDataOnWindow() {
        if (applicant == null) return;
        tfName.setText(applicant.getName());
        tfSurname.setText(applicant.getSurname());
        tfPatronymic.setText(applicant.getPatronymic());
        datePicker.setValue(applicant.getBirthday());
        chkIsPaidCheckBox.setSelected(applicant.getOnPaidBase());

        tfLangPoints.setText(applicant.getLanguagePoints().toString());
        tfSchoolPoints.setText(applicant.getSchoolMark().toString());
        tfFirstPoints.setText(applicant.getFirstSubjPoints().toString());
        tfSecondPoints.setText(applicant.getSecondSubjPoints().toString());

        int facultyId = applicant.getFacultyId();
        Optional<Faculty> faculty = faculties.stream().filter(x -> x.getId().equals(facultyId)).findFirst();
        if (faculty.isEmpty()) return;
        selectedFaculty = faculty.get();
        chbFaculty.setValue(faculty.get().getAbbreviation());
        //Заполнение списка выбранных специальностей
        Map<Integer, Integer> prioritySpec = applicant.getPrioritySpecializations();
        for(int i = 1; i <= prioritySpec.size(); i++){
            int specId = prioritySpec.get(i);
            Optional<Node> toggle = specializationList
                    .getChildren().stream()
                    .filter(x -> ((SpecializationToggle)x).getSpecialization()
                    .getId().equals(specId)).findFirst();
            if(toggle.isEmpty()) continue;
            ((SpecializationToggle)toggle.get()).select();
        }
    }

    /**
     * Добавление абитуриента
     */
    private void add() {
        try {
            //Получение значений полей для ввода,
            //если поле не валидное (пустое или не содержит число)
            //выбрасывается исключение EmptyFieldException или NumberFormatException
            String surname = Parser.getText(tfSurname);
            String name = Parser.getText(tfName);
            String patronymic = Parser.getText(tfPatronymic);
            int lang = Parser.getInt(tfLangPoints);
            int firstSubj = Parser.getInt(tfFirstPoints);
            int secondSubj = Parser.getInt(tfSecondPoints);
            int schoolPoints = Parser.getInt(this.tfSchoolPoints);

            LocalDate birthday = datePicker.getValue();
            if (birthday == null) throw new DateTimeException("null");

            boolean isPaid = chkIsPaidCheckBox.isSelected();
            //заполнение приоритета специльностей
            Map<Integer, Integer> specializationMap = new HashMap<>();
            for (Node node : specializationList.getChildren()) {
                SpecializationToggle toggle = (SpecializationToggle) node;
                if (toggle.getIndex() > 0) {
                    specializationMap.put(toggle.getIndex(), toggle.getSpecialization().getId());
                }
            }

            if (specializationMap.size() == 0) {
                new MessageBox("необходимо выбрать специальности");
                return;
            }


            String commandName = "edit-applicant";
            if (paneMode == PaneMode.ADD) {
                applicant = new Applicant();
                commandName = "add-applicant";
            }
            applicant.setName(name);
            applicant.setSurname(surname);
            applicant.setPatronymic(patronymic);
            applicant.setFacultyId(selectedFaculty.getId());
            applicant.setPrioritySpecializations(specializationMap);
            applicant.setSchoolMark(schoolPoints);
            applicant.setLanguagePoints(lang);
            applicant.setFirstSubjPoints(firstSubj);
            applicant.setSecondSubjPoints(secondSubj);
            applicant.setOnPaidBase(isPaid);
            applicant.setBirthday(birthday);

            Param param = new Param();
            param.addParameter(ParamName.APPLICANT, applicant);
            Controller.getInstance().doCommand(commandName, param);

            if (paneMode == PaneMode.ADD) {
                clear();
            } else {
                close();
            }
        } catch (DateTimeException ex) {
            new MessageBox("Ошибка ввода даты");
            return;
        } catch (EmptyFieldException ex) {
            new MessageBox("Ошибка ввода данных");
            return;
        } catch (NumberFormatException ex) {
            new MessageBox("Ошибка ввода баллов");
            return;
        }
    }

    /**
     * Очистка полей ввода
     */
    private void clear() {
        tfName.setText("");
        tfSurname.setText("");
        tfPatronymic.setText("");
        tfLangPoints.setText("");
        tfFirstPoints.setText("");
        tfSecondPoints.setText("");
        tfSchoolPoints.setText("");
        chkIsPaidCheckBox.setSelected(false);

        selectedFaculty = null;
        chbFaculty.setValue("");

        specializationList.getChildren().clear();
        pnlPoints.setVisible(false);
        SpecializationToggle.clear();
    }

    /**
     * Закрытие панели
     */
    private void close() {
        if (onClose != null) {
            onClose.handle(new Event(Event.ANY));
        }
    }

    /**
     * Перечесление указывающее тип панели:
     * Редактирование существуюзего абитуриента
     * Добавление нового абитуриента
     */
    private enum PaneMode {
        EDIT,
        ADD
    }
}
