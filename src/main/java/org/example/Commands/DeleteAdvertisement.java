package org.example.Commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Bd.BdManager;
import org.example.Interfaces.cli.Advertisement;
import org.example.Interfaces.cli.Exceptions.DefaultException;
import org.example.Interfaces.cli.User;
import org.example.Interfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class DeleteAdvertisement implements Command {
    private Outputer outputer;
    private BdManager bdManager;
    private User user;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {"advertisementId"};
    private int advertisementId;

    public DeleteAdvertisement(Outputer out, BdManager bd, User u) {
        this.outputer = out;
        this.bdManager = bd;
        this.user = u;
    }

    public void execute() {
        try {
            if (bdManager.deleteAdvertisement(advertisementId, user)) {
                outputer.outputLine("Объявление №" + advertisementId + " удалено!");
            } else {
                outputer.outputLine("У вас нет такого объявления! Проверьте корректность введенных данных!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("KeyDoesNotExistError")) {
                outputer.outputLine("У вас нет такого объявления! Проверьте корректность введенных данных!\"");
            } else if (e.getMessage().equals("UserDoesNotExist")) {
                outputer.outputLine("Вход в систему не выполнен! Войдите с помощью команды /login");
            } else {
                outputer.outputLine("Произошла непредвиденная ошибка! Попробуйте еще раз!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        if (d.containsKey("advertisementId") && NumberUtils.isCreatable(d.get("advertisementId"))) {
            data.put("advertisementId", d.get("advertisementId"));
            advertisementId = NumberUtils.toInt(data.get("advertisementId"));
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
        return "Удаляет объявление текущего пользователя" + "\n"
                + "Вид: /deleteAdvertisement advertisementId{};";
    }
}
