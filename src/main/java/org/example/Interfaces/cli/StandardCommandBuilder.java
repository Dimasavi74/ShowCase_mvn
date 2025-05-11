package org.example.Interfaces.cli;

import org.example.Bd.BdManager;
import org.example.Commands.*;
import org.example.Interfaces.cli.Exceptions.WrongInput;
import org.example.Interfaces.cli.io.Inputer;
import org.example.Interfaces.cli.io.Outputer;
import org.example.Interfaces.cli.io.Parser;

import java.util.HashMap;

public class StandardCommandBuilder implements CommandBuilder {
    private Inputer inputer;
    private Parser parser;
    private Outputer outputer;
    private BdManager bdManager;
    private User user;
    private final HashMap<String, Command> commandObjects = new HashMap<>();

    public StandardCommandBuilder(Outputer outputer, Inputer inputer, Parser parser, BdManager bdManager, User user) {
        commandObjects.put("help", new Help(outputer, commandObjects));
        commandObjects.put("exit", new Exit());
        commandObjects.put("getInfo", new GetInfo(outputer, commandObjects));
        commandObjects.put("register", new Register(outputer, bdManager));
        commandObjects.put("login", new Login(outputer, bdManager, user));
        commandObjects.put("deleteUser", new DeleteUser(outputer, bdManager));
        commandObjects.put("createAdvertisement", new CreateAdvertisement(outputer, bdManager, user));
        commandObjects.put("deleteAdvertisement", new DeleteAdvertisement(outputer, bdManager, user));
        commandObjects.put("myAdvertisements", new MyAdvertisements(outputer, bdManager, user));
        commandObjects.put("showAdvertisement", new ShowAdvertisement(outputer, bdManager));
        commandObjects.put("search", new Search(outputer, bdManager));

        this.inputer = inputer;
        this.parser = parser;
        this.outputer = outputer;
        this.bdManager = bdManager;
        this.user = user;
    }

    public Command build(String commandName, HashMap<String, String> commandArgs) {
        Command command = getCommandObject(commandName);
        command.setData(commandArgs);
        while (!command.checkCompleteness()) {

            outputer.outputLine("Некоторые обязательные поля остались незаполненными: "
                    + String.join(" ", command.getEmptyFields()));
            String newDataLine = this.inputer.getLine();
            HashMap<String, String> parsedData = this.parser.parseLine(newDataLine);
            command.setData(parsedData);
        }
        return command;
    }


    public Command getCommandObject(String commandName) {
        if (commandObjects.containsKey(commandName)) {
            return commandObjects.get(commandName);
        } else {
            throw new WrongInput(String.format("Команда %s не найдена. Список доступных команд /help;", commandName));
        }
    }

    public void setInputer(Inputer inp) {this.inputer = inp;}

    public void setParser(Parser prs) {
        this.parser = prs;
    }

    public void setOutputer(Outputer out) {
        this.outputer = out;
    }

    public Inputer getInputer() {
        return this.inputer;
    }

    public Parser getParser() {
        return this.parser;
    }

    public Outputer getOutputer() {
        return this.outputer;
    }
}
