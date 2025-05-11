package org.example.Commands;

import org.example.Bd.BdManager;
import org.example.Exceptions.DefaultException;
import org.example.UserInterfaces.cli.User;
import org.example.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdvertisements implements Command {
    private Outputer outputer;
    private BdManager bdManager;
    private User user;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {};

    public MyAdvertisements(Outputer out, BdManager bd, User u) {
        this.outputer = out;
        this.bdManager = bd;
        this.user = u;
    }

    public void execute() {
        try {
            HashMap<Integer, String> result = bdManager.userAdvertisements(user);
            if (result.isEmpty()) {
                outputer.outputLine("Вы еще не создали ни одного объявления!");
            } else {
                for (int id : result.keySet()) {
                    outputer.outputLine("Объявление №" + id + ": " + result.get(id));
                }
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("UserDoesNotExist")) {
                outputer.outputLine("Вход в систему не выполнен! Войдите с помощью команды /login");
            } else {
                outputer.outputLine("Произошла непредвиденная ошибка! Попробуйте еще раз!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        return;
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
        return "Удаляет объявление текущего пользователя" + "\n"
                + "Вид: /deleteAdvertisement advertisementId{};";
    }
}
