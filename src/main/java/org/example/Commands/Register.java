package org.example.Commands;

import org.example.Bd.BdManager;
import org.example.Exceptions.DefaultException;
import org.example.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class Register implements Command {
    private Outputer outputer;
    private BdManager bdManager;
    final String[] necessaryKeys = {"nickname", "mailAddress", "password"};
    private final HashMap<String, String> data = new HashMap<>();
    private String nickname;
    private String mailAddress;
    private String password;

    public Register(Outputer out, BdManager bd) {
        outputer = out;
        bdManager = bd;
    }

    public void execute() {
        try {
            if (bdManager.register(nickname, mailAddress, password)) {
                outputer.outputLine("Пользователь " + nickname + " успешно добавлен!");
            } else {
                outputer.outputLine("Пользователь не был добавлен! Проверьте корректность введенных данных и повторите попытку!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("KeyAlreadyExistsError")) {
                outputer.outputLine("Пользователь " + nickname + " с адресом " + mailAddress + " уже существует!");
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
                data.remove(mailAddress);
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
        return "Регистрирует нового пользователя." + "\n" + "Вид: /register nickname{} mailAddress{} password{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
