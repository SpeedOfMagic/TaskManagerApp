package team13.taskmanagerapp;

public class User extends Object{
    CharSequence login,password;

    public User(CharSequence login, CharSequence password){
        this.login=login;
        this.password=password;
    }

    public CharSequence getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public CharSequence getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
