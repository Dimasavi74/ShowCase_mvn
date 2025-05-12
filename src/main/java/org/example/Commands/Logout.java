package org.example.Commands;

import org.example.UserInterfaces.cli.User;
import org.example.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class Logout implements Command {
    private Outputer outputer;
    private User user;

    public Logout(Outputer out, User u) {
        this.outputer = out;
        this.user = u;
    }

    public void execute() {
        user.nickname = "";
        user.mailAddress = "";
        user.password = "";
        user.isLogged = false;
        outputer.outputLine("Вы вышли из аккаунта!");
    }

    public void setData(HashMap<String, String> d) {
        return;
    }

    public boolean checkCompleteness() {
        return true;
    }

    public ArrayList<String> getEmptyFields() {
        return null;
    }

    public String getInfo() {
        return "Выход из аккаунта" + "\n" + "Вид: /logout;";
    }
}
