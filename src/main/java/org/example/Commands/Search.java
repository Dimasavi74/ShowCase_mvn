package org.example.Commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Bd.BdManager;
import org.example.Exceptions.DefaultException;
import org.example.UserInterfaces.cli.io.Outputer;

import java.util.ArrayList;
import java.util.HashMap;

public class Search implements Command{
    private Outputer outputer;
    private BdManager bdManager;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {};
    private String[] words = {};
    private String[] tags = {};
    private Integer advertisementId = 0;

    public Search(Outputer out, BdManager bd) {
        this.outputer = out;
        this.bdManager = bd;
    }

    public void execute() {
        try {
            HashMap<Integer, String> result = bdManager.search(words, tags, advertisementId);
            if (result.isEmpty()) {
                outputer.outputLine("По вашему запросу ничего не найдено!1");
            } else {
                for (int id : result.keySet()) {
                    outputer.outputLine("Объявление №" + id + ": " + result.get(id));
                }
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("KeyDoesNotExistError")) {
                outputer.outputLine("По вашему запросу ничего не найдено!");
            } else {
                outputer.outputLine("Произошла непредвиденная ошибка! Попробуйте еще раз!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        if (d.containsKey("words")) {
            data.put("words", d.get("words"));
            String[] rawWords = d.get("words").split(",");
            for (int i = 0; i < rawWords.length; i++) {
                rawWords[i] = rawWords[i].strip();
            }
            tags = words;
        }
        if (d.containsKey("tags")) {
            data.put("tags", d.get("tags"));
            String[] rawTags = d.get("tags").split(",");
            for (int i = 0; i < rawTags.length; i++) {
                rawTags[i] = rawTags[i].strip();
            }
            tags = rawTags;
        }
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
        return "Ищет объявления пользователей по содержащимся в них словах, тегах, id" + "\n"
                + "Вид: /search words{w1, w2, ...} tags{t1, t2, ...} advertisementId{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
