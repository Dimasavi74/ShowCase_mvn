package org.example.Client.Commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Common.Bd.BdManager;
import org.example.Common.Advertisement;
import org.example.Common.Exceptions.DefaultException;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Common.ServerCommands.ServerSearch;
import org.example.Common.ServerCommands.ServerShowAdvertisement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ShowAdvertisement implements Command {
    private Outputer outputer;
    private Communicator communicator;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {"advertisementId"};
    private Integer advertisementId;


    public ShowAdvertisement(Outputer out, Communicator com) {
        this.outputer = out;
        this.communicator = com;
    }

    public void execute() {
        try {
            ServerShowAdvertisement showAdvertisementCommand = (ServerShowAdvertisement) communicator.executeCommand(new ServerShowAdvertisement(advertisementId));
            Advertisement advertisement = showAdvertisementCommand.advertisement;
            outputer.outputLine("Объявление №" + advertisementId + ":");
            outputer.outputLine(advertisement.title);
            outputer.outputLine(advertisement.description);
            outputer.outputLine(advertisement.price.toString() + " ₽ ");
            outputer.outputLine(advertisement.contacts);
            outputer.outputLine(Arrays.toString(advertisement.tags));
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("KeyDoesNotExistError")) {
                outputer.outputLine("Такого объявления не существует! Проверьте корректность введенных данных!\"");
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
        return "Показывает развернутую информацию об объявлении" + "\n"
                + "Вид: /showAdvertisement advertisementId{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
