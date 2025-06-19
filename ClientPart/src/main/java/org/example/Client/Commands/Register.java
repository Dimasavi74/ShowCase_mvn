package org.example.Client.Commands;

import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Client.UserInterfaces.cli.io.HeliosCommunicator;
import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Common.ServerCommands.ServerLogin;
import org.example.Common.ServerCommands.ServerRegister;

import java.util.ArrayList;
import java.util.HashMap;

public class Register implements Command {
    private Outputer outputer;
    private Communicator communicator;
    final String[] necessaryKeys = {"nickname", "mailAddress", "password"};
    private final HashMap<String, String> data = new HashMap<>();
    private String nickname;
    private String mailAddress;
    private String password;

    public Register(Outputer out, Communicator com) {
        outputer = out;
        communicator = com;
    }

    public void execute() {
        try {
            ServerRegister registerCommand = (ServerRegister) communicator.executeCommand(new ServerRegister(nickname, mailAddress, password));
            if (registerCommand.isRegistered) {
                outputer.outputLine("Пользователь " + nickname + " успешно добавлен!");
            } else {
                outputer.outputLine("Пользователь не был добавлен! Проверьте корректность введенных данных и повторите попытку!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("KeyAlreadyExistsError")) {
                outputer.outputLine("Пользователь " + nickname + " с адресом " + mailAddress + " уже существует!");
            } else {
                outputer.outputLine("Произошла непредвиденная ошибка! Попробуйте еще раз!");
            }
        }
    }

    public void setData(HashMap<String, String> d) {
        if (d.containsKey("nickname")) {
            data.put("nickname", d.get("nickname"));
            nickname = d.get("nickname");
        }
        if (d.containsKey("mailAddress")) {
            if (d.get("mailAddress").contains("@")) {
                data.put("mailAddress", d.get("mailAddress"));
                mailAddress = d.get("mailAddress");
            } else {
                data.remove(mailAddress);
                mailAddress = null;
            }
        }
        if (d.containsKey("password")) {
            data.put("password", d.get("password"));
            password = d.get("password");
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
        return "Регистрирует нового пользователя." + "\n" + "Вид: /register nickname{} mailAddress{} password{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
