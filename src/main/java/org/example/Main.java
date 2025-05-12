package org.example;

import org.example.Bd.HeliosBdManager;
import org.example.UserInterfaces.cli.StandardCommandBuilderSettings;
import org.example.UserInterfaces.cli.User;
import org.example.UserInterfaces.cli.io.Inputer;
import org.example.UserInterfaces.MainCycleController;
import org.example.UserInterfaces.cli.io.Outputer;
import org.example.UserInterfaces.cli.io.Parser;
import org.example.UserInterfaces.cli.io.ConsoleOutputer;
import org.example.UserInterfaces.cli.io.ConsoleInputer;
import org.example.UserInterfaces.cli.io.StandardParser;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        Inputer inputer = new ConsoleInputer();
        Parser parser = new StandardParser();
        Outputer outputer = new ConsoleOutputer();
        HeliosBdManager bdManager = new HeliosBdManager();
        User user = new User();
        StandardCommandBuilderSettings settings = new StandardCommandBuilderSettings();
        settings.inputMode = "line";
        MainCycleController mainCycleController = new MainCycleController(inputer, parser, outputer, bdManager, user, settings);
        mainCycleController.mainCycle();
    }
}