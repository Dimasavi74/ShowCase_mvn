package org.example;

import org.example.Bd.HeliosBdManager;
import org.example.Commands.Help;
import org.example.Interfaces.cli.User;
import org.example.Interfaces.cli.io.Inputer;
import org.example.Interfaces.MainCycleController;
import org.example.Interfaces.cli.io.Outputer;
import org.example.Interfaces.cli.io.Parser;
import org.example.Interfaces.cli.io.ConsoleOutputer;
import org.example.Interfaces.cli.io.ConsoleInputer;
import org.example.Interfaces.cli.io.StandardParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        Inputer inputer = new ConsoleInputer();
        Parser parser = new StandardParser();
        Outputer outputer = new ConsoleOutputer();
        HeliosBdManager bdManager = new HeliosBdManager();
        User user = new User();
        MainCycleController mainCycleController = new MainCycleController(inputer, parser, outputer, bdManager, user);
        mainCycleController.mainCycle();
    }
}