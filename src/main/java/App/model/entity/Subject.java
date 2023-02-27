package App.model.entity;

import java.util.Arrays;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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
