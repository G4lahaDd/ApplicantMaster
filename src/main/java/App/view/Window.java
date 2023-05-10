package App.view;

import javafx.stage.Stage;

/**
 * Интерфейс описывающий окно программы
 */
public interface Window {
    /**
     * Закрытие окна
     */
    void close();

    /**
     * Получение сцены окна
     * @return сцена
     */
    Stage getStage();
}

