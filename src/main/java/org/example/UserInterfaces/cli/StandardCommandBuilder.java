package org.example.UserInterfaces.cli;

import org.example.Bd.BdManager;
import org.example.Commands.*;
import org.example.Exceptions.DefaultException;
import org.example.UserInterfaces.MainCycleController;
import org.example.UserInterfaces.cli.io.Inputer;
import org.example.UserInterfaces.cli.io.Outputer;
import org.example.UserInterfaces.cli.io.Parser;

import java.util.HashMap;

public class StandardCommandBuilder implements CommandBuilder {
    private Inputer inputer;
    private Parser parser;
    private Outputer outputer;
    private BdManager bdManager;
    private User user;
    private final HashMap<String, Command> commandObjects = new HashMap<>();
    private StandardCommandBuilderSettings settings;
    private MainCycleController mainCycleController;

    public StandardCommandBuilder(Outputer outputer, Inputer inputer, Parser parser, BdManager bdManager, User user, StandardCommandBuilderSettings settings, MainCycleController mcController) {

        this.inputer = inputer;
        this.parser = parser;
        this.outputer = outputer;
        this.bdManager = bdManager;
        this.user = user;
        this.settings = settings;
        this.mainCycleController = mcController;

        commandObjects.put("help", new Help(outputer, commandObjects));
        commandObjects.put("exit", new Exit(mcController));
        commandObjects.put("getInfo", new GetInfo(outputer, commandObjects));
        commandObjects.put("register", new Register(outputer, bdManager));
        commandObjects.put("login", new Login(outputer, bdManager, user));
        commandObjects.put("deleteUser", new DeleteUser(outputer, bdManager));
        commandObjects.put("createAdvertisement", new CreateAdvertisement(outputer, bdManager, user));
        commandObjects.put("deleteAdvertisement", new DeleteAdvertisement(outputer, bdManager, user));
        commandObjects.put("myAdvertisements", new MyAdvertisements(outputer, bdManager, user));
        commandObjects.put("showAdvertisement", new ShowAdvertisement(outputer, bdManager));
        commandObjects.put("search", new Search(outputer, bdManager));
        commandObjects.put("changeMode", new ChangeMode(outputer, settings));
        commandObjects.put("logout", new Logout(outputer, user));
        commandObjects.put("addFavourite", new AddFavourite(outputer, bdManager, user));
        commandObjects.put("removeFavourite", new RemoveFavourite(outputer, bdManager, user));
        commandObjects.put("myFavourites", new MyFavourites(outputer, bdManager, user));
        commandObjects.put("executeFile", new ExecuteFile(outputer, inputer, parser, this));

    }

    public Command build(CommandData commandData) {
        Command command = getCommandObject(commandData.getCommandName());
        command.setData(commandData.getCommandArgs());
        if (settings.inputMode.equals("line")) {
            lineInput(command);
        } else {
            standardInput(command);
        }
        return command;
    }

    public Command lazyBuild(CommandData commandData) {
        Command command = getCommandObject(commandData.getCommandName());
        command.setData(commandData.getCommandArgs());
        if (command.checkCompleteness()) {
            return command;
        } else {
            throw new DefaultException("Некоторые обязательные поля остались незаполненными: "
                    + String.join(" ", command.getEmptyFields()));
        }
    }

    private void standardInput(Command command) {
        while (!command.checkCompleteness()) {
            outputer.outputLine("Некоторые обязательные поля остались незаполненными: "
                    + String.join(" ", command.getEmptyFields()));
            String newDataLine = this.inputer.getLine();
            CommandData parsedData = this.parser.parseLine(newDataLine);
            command.setData(parsedData.getCommandArgs());
        }
    }

    private void lineInput(Command command) {
        while (!command.checkCompleteness()) {
            outputer.outputLine("Введите обязательное поле: ");
            outputer.outputHalfLine(command.getEmptyFields().get(0) + ": ");
            String newDataLine = this.inputer.getLine();
            HashMap<String, String> newData = new HashMap<>();
            newData.put(command.getEmptyFields().get(0), newDataLine.substring(0, newDataLine.length() - 1));
            command.setData(newData);
        }
    }


    public Command getCommandObject(String commandName) {
        if (commandObjects.containsKey(commandName)) {
            return commandObjects.get(commandName);
        } else if (commandName.equals("ErrorName")) {
            throw new DefaultException("Команда не найдена. Список доступных команд /help;");
        } else {
            throw new DefaultException(String.format("Команда %s не найдена. Список доступных команд /help;", commandName));
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
