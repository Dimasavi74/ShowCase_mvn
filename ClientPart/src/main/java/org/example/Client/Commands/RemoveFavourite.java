package org.example.Client.Commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.ServerCommands.ServerRemoveFavourite;
import org.example.Common.User;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoveFavourite implements Command {
    private Outputer outputer;
    private Communicator communicator;
    private User user;
    final String[] necessaryKeys = {"advertisementId"};
    private final HashMap<String, String> data = new HashMap<>();
    private Integer advertisementId;


    public RemoveFavourite(Outputer out, Communicator com, User u) {
        this.outputer = out;
        this.communicator = com;
        this.user = u;
    }

    public void execute() {
        try {
            ServerRemoveFavourite removeFavouriteCommand = (ServerRemoveFavourite) communicator.executeCommand(new ServerRemoveFavourite(advertisementId, user));
            if (removeFavouriteCommand.isFavouriteDeleted) {
                outputer.outputLine("Объявление №" + advertisementId + " вам больше не нравится!");
            } else {
                outputer.outputLine("Объявление не было убрано! Проверьте корректность введенных данных и повторите попытку!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("UserDoesNotExist")) {
                outputer.outputLine("Вход в систему не выполнен! Войдите с помощью команды /login");
            } else if (e.getMessage().equals("SQLError")) {
                outputer.outputLine("Ошибка при работе с базой данных! Попробуйте езе раз");
            } else if (e.getMessage().equals("KeyAlreadyExistsError")) {
                outputer.outputLine("Этого объявления нет в понравившихся!");
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
        return "Убирает объявление из списка понравившихся" + "\n" +
                "Вид: /removeFavourite advertisementId{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
