package org.example.Client.Commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.example.Client.UserInterfaces.cli.io.Communicator;
import org.example.Client.UserInterfaces.cli.io.Outputer;
import org.example.Common.Bd.BdManager;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.ServerCommands.ServerAddFavourite;
import org.example.Common.ServerCommands.ServerMyFavourites;
import org.example.Common.User;

import java.util.HashMap;

public class AddFavourite implements Command {
    private Outputer outputer;
    private Communicator communicator;
    private User user;
    final String[] necessaryKeys = {"advertisementId"};
    private final HashMap<String, String> data = new HashMap<>();
    private Integer advertisementId;


    public AddFavourite(Outputer out,  Communicator com, User u) {
        this.outputer = out;
        this.communicator = com;
        this.user = u;
    }

    public void execute() {
        try {
            ServerAddFavourite addFavouriteCommand = (ServerAddFavourite) communicator.executeCommand(new ServerAddFavourite(advertisementId, user));
            if (addFavouriteCommand.isFavouriteAdded) {
                outputer.outputLine("Объявление №" + advertisementId + " успешно добавлено в \"Понравившееся\"");
            } else {
                outputer.outputLine("Объявление не было добавлено! Проверьте корректность введенных данных и повторите попытку!");
            }
        } catch (DefaultException e) {
            if (e.getMessage().equals("ConnectionIsClosedError")) {
                outputer.outputLine("Соединение с сервером разорвано!");
            } else if (e.getMessage().equals("UserDoesNotExist")) {
                outputer.outputLine("Вход в систему не выполнен! Войдите с помощью команды /login");
            } else if (e.getMessage().equals("KeyAlreadyExistsError")) {
                outputer.outputLine("Это объявление уже добавлено!");
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
        return "Добавляет объявление в список понравившихся" + "\n" +
                "Вид: /addFavourite advertisementId{};";
    }

    public String[] getNesessaryKeys() {
        return this.necessaryKeys;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }

}
