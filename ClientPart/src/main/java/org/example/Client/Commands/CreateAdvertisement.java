package org.example.Client.Commands;

import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.Common.Advertisement;
import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.User;

import java.util.HashMap;

public class CreateAdvertisement implements Command{
    private Outputer outputer;
    private BdManager bdManager;
    private User user;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {"title", "description", "price", "contacts", "tags"};
    private String title;
    private String description;
    private Integer price;
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
        if (d.containsKey("price")) {
            if (NumberUtils.isCreatable(d.get("price"))) {
                data.put("price", d.get("price"));
                price = NumberUtils.toInt(data.get("price"));
            } else {
                data.remove("price");
                price = null;
            }
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

    public String getInfo() {
        return "Создает объявление от текущего пользователя" + "\n"
                + "Вид: /createAdvertisement title{} description{} price{} contacts{} tags{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
