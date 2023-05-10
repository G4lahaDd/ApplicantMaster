package App.model.service;

import App.model.entity.Applicant;
import App.model.entity.groups.FacultyThread;
import App.model.entity.groups.Group;
import App.model.entity.groups.SpecializationThread;
import App.model.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Класс для работы с Excel форматом
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class ExcelService {

    private static final Logger LOG = LogManager.getLogger(ExcelService.class);
    private static final ExcelService INSTANCE = new ExcelService();

    private ExcelService() {
    }

    /**
     * Получение единственного экземпляра класса
     * @return Единственный экземпляр класса
     */
    public static ExcelService getInstance() {
        return INSTANCE;
    }

    /**
     * Экспорт созданных групп в Excel документы
     * @param directory Директория для сохранения
     * @throws ServiceException Ошибка выполнения
     */
    public void export(File directory) throws ServiceException {
        //Проверка директории
        if (directory == null || !directory.isDirectory() || !directory.exists()) {
            throw new ServiceException("Неправильный путь сохранения файла");
        }
        if (ApplicationDataService.getInstance().getFacultyThreads() == null) {
            throw new ServiceException("Списки не созданы");
        }
        List<FacultyThread> faculties = ApplicationDataService.getInstance().getFacultyThreads();

        try {
            //Создание папки для сохранения
            directory = new File(directory.getAbsolutePath() + "\\Списки по факультетам");
            if(!directory.exists()){
                directory.mkdir();
            }
            //Создание файлов с группами
            for (FacultyThread faculty : faculties) {
                FileOutputStream fileOut = new FileOutputStream(directory.getAbsolutePath() + "\\" + faculty.getAbbreviation() + ".xls");
                HSSFWorkbook workbook = createFile(faculty);
                workbook.write(fileOut);
                fileOut.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServiceException("Ошибка создания списков");
        }
    }

    /**
     * Создание файла для факультета
     * @param facultyThread Поток групп и студентов факультета
     * @return Файл типа Excel
     */
    private HSSFWorkbook createFile(FacultyThread facultyThread) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        for (SpecializationThread spec : facultyThread.getSpecializations()){
            HSSFSheet sheet = workbook.createSheet(spec.getName());
            fillSheet(spec, sheet);
        }
        return workbook;
    }

    /**
     * Заполнение страницы документа группами специальности
     * @param specialization Поток студентов специальности
     * @param sheet Страница для заполнения
     */
    private void fillSheet(SpecializationThread specialization, HSSFSheet sheet) {
        //Задание стиля для ячейки
        HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        //Индекс текущей строки
        int index = 0;

        //Строчка с названием специальности
        sheet.createRow(index);
        sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
        HSSFCell title = sheet.getRow(index).createCell(0);
        title.setCellValue(specialization.getName());
        title.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        index+=2;

        //Запись групп
        for (Group group : specialization.getGroups()) {
            // Строка с именем группы
            sheet.createRow(index);
            sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
            title = sheet.getRow(index).createCell(0);
            title.setCellValue("гр. " + group.getCode());
            title.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            index++;

            //Строка с заголовками
            sheet.createRow(index).createCell(0).setCellStyle(style);
            sheet.getRow(index).getCell(0).setCellValue("ФИО:");
            sheet.getRow(index).createCell(1).setCellStyle(style);
            sheet.getRow(index).getCell(1).setCellValue("Дата:");
            sheet.getRow(index).createCell(2).setCellStyle(style);
            sheet.getRow(index).getCell(2).setCellValue("Баллы:");
            index++;

            //Заполнение студентов
            for (Applicant applicant : group.getApplicants()) {
                sheet.createRow(index).createCell(0).setCellValue(applicant.getInitials());
                sheet.getRow(index).getCell(0).setCellStyle(style);
                sheet.getRow(index).createCell(1).setCellValue(applicant.getBirthday().toString());
                sheet.getRow(index).getCell(1).setCellStyle(style);
                sheet.getRow(index).createCell(2).setCellValue(applicant.getTotalMark());
                sheet.getRow(index).getCell(2).setCellStyle(style);
                index++;
            }
            index++;//Отступ для следующей группы

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
        }
    }
}
