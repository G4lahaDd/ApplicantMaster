package App.view.controls;

import App.controller.Controller;
import javafx.scene.control.Button;

/**
 * Класс, описывающий кнопку, которая отправляет запрос к контроллеру на нажатие
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class MyButton extends Button {

    private String command;

    /**
     * Конструктов кнопки по умолчанию
     */
    public MyButton() {
        super();
        setOnAction(e -> onClick());
        getStyleClass().add("custom-button");
    }

    /**
     * Конструктор кнопки с содержимым и выполняемой командой
     * @param text Текст кнопки
     * @param command Выполняемая кнопка
     */
    public MyButton(String text, String command){
        super();
        setText(text);
        this.command = command;
        setOnAction(e -> onClick());
        getStyleClass().add("custom-button");
    }

    /**
     * Получение названия команды
     * @return Название выполняемой команды
     */
    public String getCommand() {
        return command;
    }

    /**
     * Установка команды
     * @param command Название выполняемой команды
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Метод, вызываемый на нажатие кнопки
     */
    private void onClick(){
        Controller.getInstance().doCommand(command);
    }
}