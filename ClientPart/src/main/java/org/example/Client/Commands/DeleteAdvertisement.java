package org.example.Client.Commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.ServerCommands.ServerDeleteAdvertisement;
import org.example.Common.User;

import java.util.HashMap;

public class DeleteAdvertisement implements Command {
    private Outputer outputer;
    private Communicator communicator;
    private User user;
    private final HashMap<String, String> data = new HashMap<>();
    final String[] necessaryKeys = {"advertisementId"};
    private Integer advertisementId;

    public DeleteAdvertisement(Outputer out, Communicator com, User u) {
        this.outputer = out;
        this.communicator = com;
        this.user = u;
    }

    public void execute() {
        try {
            ServerDeleteAdvertisement deleteAdvertisementCommand = (ServerDeleteAdvertisement) communicator.executeCommand(new ServerDeleteAdvertisement(advertisementId, user));
            if (deleteAdvertisementCommand.isAdvertisementDeleted) {
                outputer.outputLine("Объявление №" + advertisementId + " удалено!");
            } else {
                outputer.outputLine("У вас нет такого объявления! Проверьте корректность введенных данных!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("KeyDoesNotExistError")) {
                outputer.outputLine("У вас нет такого объявления! Проверьте корректность введенных данных!\"");
            } else if (e.getMessage().equals("SQLError")) {
                outputer.outputLine("Ошибка при работе с базой данных! Попробуйте езе раз");
            } else if (e.getMessage().equals("UserDoesNotExist")) {
                outputer.outputLine("Вход в систему не выполнен! Войдите с помощью команды /login");
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
        return "Удаляет объявление текущего пользователя" + "\n"
                + "Вид: /deleteAdvertisement advertisementId{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }
}
