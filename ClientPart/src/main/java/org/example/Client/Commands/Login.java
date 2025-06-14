package org.example.Client.Commands;

import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Login implements Command{
    private Outputer outputer;
    private BdManager bdManager;
    private Communicator communicator;
    private User user;
    final String[] necessaryKeys = {"nickname", "mailAddress", "password"};
    private final HashMap<String, String> data = new HashMap<>();
    private String nickname;
    private String mailAddress;
    private String password;

    public Login(Outputer out, BdManager bd, Communicator com, User u) {
        outputer = out;
        bdManager = bd;
        user = u;
        communicator = com;
    }

    public void execute() {
        try {
            if (bdManager.login(nickname, mailAddress, password)) {
                user.nickname = nickname;
                user.mailAddress = mailAddress;
                user.password = password;
                user.isLogged = true;
                outputer.outputLine("Вход успешно выполнен!");
            } else {
                outputer.outputLine("Такого пользователя не существует! Проверьте корректность введенных данных!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else {
                outputer.outputLine("Произошла непредвиденная ошибка! Попробуйте еще раз!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        if (d.containsKey("nickname")) {
            data.put("nickname", d.get("nickname"));
            nickname = d.get("nickname");
        }
        if (d.containsKey("mailAddress")) {
            if (d.get("mailAddress").contains("@")) {
                data.put("mailAddress", d.get("mailAddress"));
                mailAddress = d.get("mailAddress");
            } else {
                data.remove("mailAddress");
                mailAddress = null;
            }
        }
        if (d.containsKey("password")) {
            data.put("password", d.get("password"));
            password = d.get("password");
        }
    }

    public boolean checkCompleteness() {
        for (String el: necessaryKeys) {
            if (data.get(el) == null) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getEmptyFields() {
        ArrayList<String> emptyFields = new ArrayList<>();
        for (String el: necessaryKeys) {
            if (data.get(el) == null) {
                emptyFields.add(el);
            }
        }
        return emptyFields;
    }

    public String getInfo() {
        return "Выполняет вход" + "\n" + "Вид: /login nickname{} mailAddress{} password{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
