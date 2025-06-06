package org.example.Commands;

import org.example.Bd.BdManager;
import org.example.Interfaces.cli.Exceptions.DefaultException;
import org.example.Interfaces.cli.User;
import org.example.Interfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class DeleteUser implements Command{
    private Outputer outputer;
    private BdManager bdManager;
    final String[] necessaryKeys = {"nickname", "mailAddress", "password"};
    private final HashMap<String, String> data = new HashMap<>();
    private String nickname;
    private String mailAddress;
    private String password;

    public DeleteUser(Outputer out, BdManager bd) {
        outputer = out;
        bdManager = bd;
    }


    public void execute() {
        try {
            if (bdManager.deleteUser(nickname, mailAddress, password)) {
                outputer.outputLine("Пользователь удален!");
            } else {
                outputer.outputLine("Такого пользователя не существует! Проверьте корректность введенных данных!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("KeyDoesNotExistError")) {
                outputer.outputLine("Такого пользователя не существует! Проверьте корректность введенных данных!\"");
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
        if (d.containsKey("mailAddress") && d.get("mailAddress").contains("@")) {
            data.put("mailAddress", d.get("mailAddress"));
            mailAddress = d.get("mailAddress");
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
        return "Удаляет аккаунт пользователя" + "\n" + "Вид: /deleteUser nickname{} mailAddress{} password{};";
    }
}
