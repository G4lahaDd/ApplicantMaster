package App.model.service;

import App.model.dao.ApplicantDao;
import App.model.dao.exception.DaoException;
import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.entity.groups.FacultyThread;
import App.model.entity.groups.SpecializationThread;
import App.model.service.exception.ServiceException;
import App.model.service.validator.ApplicantValidator;
import App.model.service.validator.Validator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Класс бизнес логики для абитуриентов
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ApplicantService {

    private static final ApplicationDataService appData = ApplicationDataService.getInstance();
    private static final ApplicantDao appDao = ApplicantDao.getInstance();
    private static final Validator appValidator = ApplicantValidator.getInstance();
    /**
     * Единственный экземпляр класса
     */
    private static final ApplicantService INSTANCE = new ApplicantService();

    /**Список текущих рассматриваемых абитуриентов*/
    private List<Applicant> current;

    /**
     * Список непоступивших абитуриентов
     */
    private List<Applicant> nonAccepted;

    private ApplicantService() {
        current = new ArrayList<>();
        nonAccepted = new ArrayList<>();
    }

    /**
     * Получение единственного экземпляра класса.
     * @return Единственный экземпляр класса.
     */
    public static ApplicantService getInstance() {
        return INSTANCE;
    }

    /**
     * Получение абитуриентов удовлетворяющих условию фильтра
     * @param filter фильтр запроса, содержащий SQL синтаксис
     * @return Лист абитуриентов
     * @throws ServiceException Ошибка выполнения метода или ошибка сети
     */
    public List<Applicant> getApplicants(String filter) throws ServiceException {
        List<Applicant> result;
        try {
            if (filter.isEmpty() || filter == null) {
                result = appDao.getAllApplicant();
            } else {
                result = appDao.getAllApplicantByFilter(filter);
            }
        } catch (DaoException ex) {
            System.out.println("Error while get applicants with filter = " + filter + ex.getMessage());
            throw new ServiceException("Error while get applicants with filter = " + filter);
        }
        return result;
    }

    /**
     * Создание групп студентов из абитуриентов
     * @return Лист потоков студентов факультетов
     * @throws ServiceException Ошибка выполнения метода или ошибка сети
     */
    public List<FacultyThread> createGroups() throws ServiceException {
        List<Faculty> faculties = appData.getFaculties();
        List<FacultyThread> result = new ArrayList<>();
        //Заполнение всех факультетов студентами
        for (Faculty faculty : faculties) {
            FacultyThread facultyThread = fillFacultyThred(faculty);
            // Создание групп факультета
            facultyThread.CreateGroups();
            result.add(facultyThread);
        }
        System.out.println(nonAccepted.size());
        ApplicationDataService.getInstance().setFacultyThreads(result);
        return result;
    }

    /**
     * Заполнение факультета студентами
     * @param faculty факультет
     * @return поток студентов факультета
     * @throws ServiceException Ошибка выполнения метода или ошибка сети
     */
    private FacultyThread fillFacultyThred(Faculty faculty) throws ServiceException {
        // Поиск абитуриентов относящихся к факультету
        String filter = " faculty_id = " + faculty.getId();
        List<Applicant> applicants = getApplicants(filter);

        //Сортировка всех абитуриентов по баллу
        current.clear();
        current.addAll(applicants.stream()
                .sorted(Comparator.comparingInt(Applicant::getTotalMark)
                        .reversed()).toList());

        //Заполнение факультета спеуциальностями
        FacultyThread facultyThread = new FacultyThread(faculty);
        for (Specialization spec : faculty.getSpecializations()) {
            facultyThread.addSpecialization(new SpecializationThread(spec));
        }

        //Заполнение абитуриентов по специальностям пока количество не станет равно 0
        Applicant applicant;
        List<SpecializationThread> specializations;
        while(current.size() > 0){
            boolean find = false;
            applicant = current.get(0);//Первый абитуриент из списка
            //Получение желаемых специальностей абитуриента
            specializations = facultyThread.GetSpecializationByPriorityList(applicant.getPrioritySpecializations());
            for(int j = 0; j < specializations.size(); j++){
                //Если есть свободное место, идёт добавление
                if(addApplicantToSpecialization(applicant,specializations.get(j),false)){
                    find = true;
                    break;
                }
            }
            if(find) continue;
            //Если абитуриент не нашёл место на бюджете, но рассматривает платную основу
            //Поиск из платных мест
            if(applicant.getOnPaidBase()){
                for(int j = 0; j < specializations.size(); j++){
                    if(addApplicantToSpecialization(applicant,specializations.get(j),true)){
                        find = true;
                        break;
                    }
                }
            }
            if(!find){
                //Если не найдена специальность, то абитуриент не поступил
                current.remove(applicant);
                nonAccepted.add(applicant);
            }
        }

        return facultyThread;
    }

    /**
     * Добавление абитуриента в базу данных
     * @param applicant абитуриент
     * @return true - если успешно, иначе false
     * @throws ServiceException Ошибка выполнения метода или ошибка сети
     */
    public boolean addApplicant(Applicant applicant) throws ServiceException{
        try{
            if(appValidator.isValid(applicant)){
                appDao.addApplicant(applicant);
                return true;
            }
            else{
                System.out.println("Неверные данные абитуриента." + applicant);
                return false;
            }
        }catch (DaoException ex){
            throw new ServiceException("Error while add applicant \n\t" + ex.getMessage());
        }
    }

    /**
     * Обновление данных абитуриента
     * @param applicant абитуриент
     * @return true - если успешно, иначе false
     * @throws ServiceException Ошибка выполнения метода или ошибка сети
     */
    public boolean updateApplicant(Applicant applicant) throws ServiceException{
        try{
            if(appValidator.isValid(applicant)){
                appDao.updateApplicant(applicant);
                return true;
            }
            else{
                System.out.println("Неверные данные абитуриента." + applicant);
                return false;
            }
        }catch (DaoException ex){
            throw new ServiceException("Service: Error while update applicant \n\t" + ex.getMessage());
        }
    }

    /**
     * Удаление абитуриента
     * @param applicant абитуриент
     * @throws ServiceException Ошибка выполнения метода или ошибка сети
     */
    public void deleteApplicant(Applicant applicant) throws ServiceException{
        try{
            if(applicant.getId() != null && applicant.getId() > 0)
                appDao.deleteApplicant(applicant.getId());
        }catch (DaoException ex){
            throw new ServiceException("Error while delete applicant \n\t" + ex.getMessage());
        }
    }

    /**
     * Добавление абитуриента к специальности
     * @param applicant Абитуриент
     * @param specialization Поток студентов специальности
     * @param onPaid На какую основу добавлять
     * @return true - если успешно, иначе false
     */
    public boolean addApplicantToSpecialization(Applicant applicant, SpecializationThread specialization, boolean onPaid) {
        int places;
        if (onPaid) places = specialization.getPaidPlaces();
        else places = specialization.getBudgedPlaces();
        if (places == 0) {
            //Если мест нет, то проверка полупроходного балла с последним добавленным студентом
            Applicant last = specialization.getApplicants().get(specialization.getApplicants().size() - 1);
            if (last.getTotalMark() != applicant.getTotalMark()) return false;
            //Сравнение баллов по первому предмету специальности
            if (applicant.getFirstSubjPoints() > last.getFirstSubjPoints()) {
                specialization.replaceLast(applicant);
                current.remove(applicant);
                current.add(0,last);
                return true;
            }
        } else {
            //Добавление абитуриента в группу
            specialization.addApplicant(applicant, onPaid);
            applicant.setOnPaidBase(onPaid);
            current.remove(applicant);
            return true;
        }
        return false;
    }
}

