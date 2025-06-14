package org.example.Commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Bd.BdManager;
import org.example.Common.Advertisement;
import org.example.Exceptions.DefaultException;
import org.example.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ShowAdvertisement implements Command {
    private Outputer outputer;
    private BdManager bdManager;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {"advertisementId"};
    private Integer advertisementId;


    public ShowAdvertisement(Outputer out, BdManager bd) {
        this.outputer = out;
        this.bdManager = bd;
    }

    public void execute() {
        try {
            Advertisement advertisement = bdManager.showAdvertisement(advertisementId);
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
