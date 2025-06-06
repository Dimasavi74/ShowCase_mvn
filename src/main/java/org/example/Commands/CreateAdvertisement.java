package org.example.Commands;

import org.example.Bd.BdManager;
import org.example.Interfaces.cli.Advertisement;
import org.example.Interfaces.cli.Exceptions.DefaultException;
import org.example.Interfaces.cli.User;
import org.example.Interfaces.cli.io.Outputer;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateAdvertisement implements Command{
    private Outputer outputer;
    private BdManager bdManager;
    private User user;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {"title", "description", "price", "contacts"};
    private String title;
    private String description;
    private int price;
    private String contacts;
    private String[] tags = {};

    public CreateAdvertisement(Outputer out, BdManager bd, User u) {
        this.outputer = out;
        this.bdManager = bd;
        this.user = u;
    }

    public void execute() {
        try {
            Advertisement advertisement = new Advertisement(title, description, price, contacts, tags);
            int id = bdManager.createAdvertisement(advertisement, user);
            if (id != 0) {
                outputer.outputLine("Объявление №" + id + " успешно создано!");
            } else {
                outputer.outputLine("Объявление не было создано! Проверьте корректность введенных данных и повторите попытку!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("UserDoesNotExist")) {
                outputer.outputLine("Вход в систему не выполнен! Войдите с помощью команды /login");
            } else if (e.getMessage().equals("AdvertisementAlreadyExists")) {
                outputer.outputLine("Такое объявление уже существует!");
            } else if (e.getMessage().equals("TagAlreadyExists")) {
                outputer.outputLine("Такой тег уже существует!");
            } else {
                outputer.outputLine("Произошла непредвиденная ошибка! Попробуйте еще раз!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        if (d.containsKey("title")) {
            data.put("title", d.get("title"));
            title = d.get("title");
        }
        if (d.containsKey("description")) {
            data.put("description", d.get("description"));
            description = d.get("description");
        }
        if (d.containsKey("price") && NumberUtils.isCreatable(d.get("price"))) {
            data.put("price", d.get("price"));
            price = NumberUtils.toInt(data.get("price"));
        }
        if (d.containsKey("contacts")) {
            data.put("contacts", d.get("contacts"));
            contacts = d.get("contacts");
        }
        if (d.containsKey("tags")) {
            data.put("tags", d.get("tags"));
            String[] rawTags = d.get("tags").split(",");
            for (int i = 0; i < rawTags.length; i++) {
                rawTags[i] = rawTags[i].strip();
            }
            tags = rawTags;
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
        return "Создает объявление от текущего пользователя" + "\n"
                + "Вид: /createAdvertisement title{} description{} price{} contacts{} tags{};";
    }
}
