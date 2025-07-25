package org.example.Client.Commands;


import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.ServerCommands.ServerDeleteUser;

import java.util.HashMap;

public class DeleteUser implements Command{
    private Outputer outputer;
    private Communicator communicator;
    final String[] necessaryKeys = {"nickname", "mailAddress", "password"};
    private final HashMap<String, String> data = new HashMap<>();
    private String nickname;
    private String mailAddress;
    private String password;

    public DeleteUser(Outputer out, Communicator com) {
        outputer = out;
        communicator = com;
    }


    public void execute() {
        try {
            ServerDeleteUser deleteUserCommand = (ServerDeleteUser) communicator.executeCommand(new ServerDeleteUser(nickname, mailAddress, password));
            if (deleteUserCommand.isDeleted) {
                outputer.outputLine("Пользователь удален!");
            } else {
                outputer.outputLine("Такого пользователя не существует! Проверьте корректность введенных данных!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("SQLError")) {
                outputer.outputLine("Ошибка при работе с базой данных! Попробуйте езе раз");
            } else if (e.getMessage().equals("KeyDoesNotExistError")) {
                outputer.outputLine("Такого пользователя не существует! Проверьте корректность введенных данных!\"");
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
                data.remove("mailAddress");
                mailAddress = null;
            }
        }
        if (d.containsKey("password")) {
            data.put("password", d.get("password"));
            password = d.get("password");
        }
    }


    public String getInfo() {
        return "Удаляет аккаунт пользователя" + "\n" + "Вид: /deleteUser nickname{} mailAddress{} password{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
