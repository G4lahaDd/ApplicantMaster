package App.model.entity;

import java.util.Objects;

/**
 * Класс, описывающий пользователя
 *
 * @author Kazyro I.A.
 * @version 1.0
 */
public class User {
    private Integer id;
    private String login;
    private String password;
    private String mail;

    /**
     * Метод возвращает идентификатор пользователя.
     * @return идентификатор пользователя
     */
    public Integer getId() {
        return id;
    }
    /**
     * Метод устанавливает идентификатор пользователя.
     * @param id идентификатор пользователя
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * Метод возвращает логин пользователя.
     * @return логин пользователя
     */
    public String getLogin() {
        return login;
    }
    /**
     * Метод устанавливает логин пользователя.
     * @param login логин пользователя
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /**
     * Метод возвращает пароль пользователя.
     * @return пароль пользователя
     */
    public String getPassword() {
        return password;
    }
    /**
     * Метод устанавливает пароль пользователя.
     * @param password пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Метод возвращает электронную почту пользователя.
     * @return электронная почта пользователя
     */
    public String getMail() {
        return mail;
    }
    /**
     * Метод устанавливает электронную почту пользователя.
     * @param mail электронная почта пользователя
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && login.equals(user.login) && password.equals(user.password) && mail.equals(user.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, mail);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
