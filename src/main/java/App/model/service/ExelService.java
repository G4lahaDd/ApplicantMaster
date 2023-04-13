package App.model.service;

import App.model.entity.Applicant;
import App.model.entity.Faculty;
import App.model.entity.Specialization;
import App.model.entity.Subject;
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

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExelService {

    //region TEST
    public List<String> surnames = new ArrayList<>();
    public List<String> names = new ArrayList<>();
    public List<String> patronymics = new ArrayList<>();
    //endregion

    public final List<Faculty> faculties = ApplicationDataService.getInstance().getFaculties();
    private static final Logger LOG = LogManager.getLogger(ExelService.class);
    private static final ExelService INSTANCE = new ExelService();

    private ExelService() {
    }

    public static ExelService getInstance() {
        return INSTANCE;
    }

    //region Test
    public void readInitialsFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            //чтение построчно
            String s;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
                String[] initials = s.split(" ");
                if (initials.length < 3) continue;
                surnames.add(initials[0]);
                names.add(initials[1]);
                patronymics.add(initials[2]);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("sur count: " + surnames.size());
        System.out.println("name count: " + names.size());
        System.out.println("patr count: " + patronymics.size());
    }

    public Applicant getRandomApplicant() {
        Applicant applicant = new Applicant();
        Random r = new Random();
        applicant.setSurname(surnames.get(r.nextInt(surnames.size())));
        applicant.setName(names.get(r.nextInt(names.size())));
        applicant.setPatronymic(patronymics.get(r.nextInt(patronymics.size())));

        applicant.setSchoolMark(r.nextInt(40, 100));
        applicant.setLanguagePoints(r.nextInt(40, 100));
        applicant.setFirstSubjPoints(r.nextInt(40, 100));
        applicant.setSecondSubjPoints(r.nextInt(40, 100));

        applicant.setOnPaidBase(r.nextInt(10) >= 7);

        Faculty faculty = faculties.get(r.nextInt(faculties.size()));
        applicant.setFacultyId(faculty.getId());

        int size = faculty.getSpecializations().size();
        List<Specialization> specializations = new ArrayList<>();
        specializations.addAll(faculty.getSpecializations());
        Specialization specialization = specializations.get(r.nextInt(size));
        Subject first = specialization.getFirstSubject();
        Subject second = specialization.getSecondSubject();
        for (int i = size - 1; i >= 0; i--) {
            Specialization spec = specializations.get(i);
            if (spec.getFirstSubject() != first
                    || spec.getSecondSubject() != second) {
                specializations.remove(spec);
            }
        }
        size = specializations.size();
        int count = r.nextInt(size + 1);
        for (int i = 1; i <= count; i++) {
            size = specializations.size();
            Specialization spec = specializations.get(r.nextInt(size));
            applicant.getPrioritySpecializations().put(i, spec.getId());
            specializations.remove(spec);
        }

        applicant.setBirthday(LocalDate.of(2005, r.nextInt(1, 12), r.nextInt(1, 27)));

        return applicant;
    }

    //endregion
    public void export(File directory) throws ServiceException {
        if (directory == null || !directory.isDirectory() || !directory.exists()) {
            throw new ServiceException("Неправильный путь сохранения файла");
        }
        if (ApplicationDataService.getInstance().getFacultyThreads() == null) {
            throw new ServiceException("Списки не созданы");
        }
        List<FacultyThread> faculties = ApplicationDataService.getInstance().getFacultyThreads();

        try {
            directory = new File(directory.getAbsolutePath() + "\\Списки по факультетам");
            if(!directory.exists()){
                directory.mkdir();
            }
            for (FacultyThread faculty : faculties) {
                FileOutputStream fileOut = new FileOutputStream(directory.getAbsolutePath() + "\\" + faculty.getAbbreviation() + ".xls");
                HSSFWorkbook workbook = createFile(faculty);
                workbook.write(fileOut);
                fileOut.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            //Обработка ошибки
        }
    }

    private HSSFWorkbook createFile(FacultyThread facultyThread) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        for (SpecializationThread spec : facultyThread.getSpecializations()){
            HSSFSheet sheet = workbook.createSheet(spec.getName());
            fillSheet(spec, sheet);
        }
        return workbook;
    }

    private void fillSheet(SpecializationThread specialization, HSSFSheet sheet) {
        HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        int index = 0;

        sheet.createRow(index);
        sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
        HSSFCell title = sheet.getRow(index).createCell(0);
        title.setCellValue(specialization.getName());
        title.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        index+=2;

        for (Group group : specialization.getGroups()) {
            sheet.createRow(index);
            sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
            title = sheet.getRow(index).createCell(0);
            title.setCellValue("гр. " + group.getCode());
            title.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            index++;

            sheet.createRow(index).createCell(0).setCellStyle(style);
            sheet.getRow(index).getCell(0).setCellValue("ФИО:");
            sheet.getRow(index).createCell(1).setCellStyle(style);
            sheet.getRow(index).getCell(1).setCellValue("Дата:");
            sheet.getRow(index).createCell(2).setCellStyle(style);
            sheet.getRow(index).getCell(2).setCellValue("Баллы:");
            index++;

            for (Applicant applicant : group.getApplicants()) {
                sheet.createRow(index).createCell(0).setCellValue(applicant.getInitials());
                sheet.getRow(index).getCell(0).setCellStyle(style);
                sheet.getRow(index).createCell(1).setCellValue(applicant.getBirthday().toString());
                sheet.getRow(index).getCell(1).setCellStyle(style);
                sheet.getRow(index).createCell(2).setCellValue(applicant.getTotalMark());
                sheet.getRow(index).getCell(2).setCellStyle(style);
                index++;
            }
            index++;

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
        }
    }



    private void setTextSize(HSSFCell cell, int size) {
        cell.getCellStyle().getFont(cell.getSheet().getWorkbook()).setFontHeight((short) size);
    }

}
