package App.model.entity;

/**
 * Перечисление, описывающее предметы
 *
 * @author Kazyro I.A
 * @version 1.0
 */
public enum Subject {
    Math(0,"Математика"),
    Physics(1, "Физика"),
    English(2, "Англ.яз"),
    Chemistry(3,"Химия"),
    History(4,"История"),
    Biology(5,"Биология");

    private int code;
    private String name;

    Subject(int code, String name){
        this.code = code;
        this.name = name;
    }

    /**
     * Получение кода предмета
     */
    public int getCode() {
        return code;
    }

    /**
     * Получение предмета по его коду
     * @param code код предмета
     * @return предмет
     */
    public static Subject valueOf(int code){
        Subject[] subjects = Subject.values();
        for (int i = 0; i < subjects.length; i++) {
            if(subjects[i].code == code) return subjects[i];
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
