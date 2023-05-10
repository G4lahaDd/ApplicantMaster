package App.view.controls;

/**
 * Интерфейс, необходимый для окон и компонентов,
 * которые требуют обновление данных при их открытии
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public interface Refreshable {
    /**
     * Обновление данных
     */
    void refresh();
}
