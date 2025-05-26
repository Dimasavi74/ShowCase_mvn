package org.example.Commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Bd.BdManager;
import org.example.Exceptions.DefaultException;
import org.example.UserInterfaces.cli.Advertisement;
import org.example.UserInterfaces.cli.User;
import org.example.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class AddFavourite implements Command {
    private Outputer outputer;
    private BdManager bdManager;
    private User user;
    final String[] necessaryKeys = {"advertisementId"};
    private final HashMap<String, String> data = new HashMap<>();
    private Integer advertisementId;


    public AddFavourite(Outputer out, BdManager bd, User u) {
        this.outputer = out;
        this.bdManager = bd;
        this.user = u;
    }

    public void execute() {
        try {
            if (bdManager.addFavourite(user, advertisementId)) {
                outputer.outputLine("Объявление №" + advertisementId + " успешно добавлено в \"Понравившееся\"");
            } else {
                outputer.outputLine("Объявление не было добавлено! Проверьте корректность введенных данных и повторите попытку!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("UserDoesNotExist")) {
                outputer.outputLine("Вход в систему не выполнен! Войдите с помощью команды /login");
            } else if (e.getMessage().equals("KeyAlreadyExistsError")) {
                outputer.outputLine("Это объявление уже добавлено!");
            } else {
                outputer.outputLine("Произошла непредвиденная ошибка! Попробуйте еще раз!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        if (d.containsKey("advertisementId")) {
            if (NumberUtils.isCreatable(d.get("advertisementId"))) {
                data.put("advertisementId", d.get("advertisementId"));
                advertisementId = NumberUtils.toInt(data.get("advertisementId"));
            } else {
                data.remove("advertisementId");
                advertisementId = null;
            }
        }
    }

    public String getInfo() {
        return "Добавляет объявление в список понравившихся" + "\n" +
                "Вид: /addFavourite advertisementId{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }

}
