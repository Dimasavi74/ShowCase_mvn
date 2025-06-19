package org.example.Client.Commands;

import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.ServerCommands.ServerMyAdvertisements;
import org.example.Common.ServerCommands.ServerMyFavourites;
import org.example.Common.User;
import org.example.Client.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class MyFavourites implements Command {
    private Outputer outputer;
    private Communicator communicator;
    private User user;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {};

    public MyFavourites(Outputer out, Communicator com, User u) {
        this.outputer = out;
        this.communicator = com;
        this.user = u;
    }

    public void execute() {
        try {
            ServerMyFavourites myFavouritesCommand = (ServerMyFavourites) communicator.executeCommand(new ServerMyFavourites(user));
            HashMap<Integer, String> result = myFavouritesCommand.userFavourites;
            if (result.isEmpty()) {
                outputer.outputLine("Вы еще не добавили ни одного объявления!");
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
        return "Показывает понравившиеся объявления текущего пользователя" + "\n"
                + "Вид: /myAdvertisements;";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}