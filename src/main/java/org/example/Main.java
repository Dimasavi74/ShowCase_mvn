package org.example;

import org.example.Bd.HeliosBdManager;
import org.example.Server.ServerMain;
import org.example.UserInterfaces.cli.StandardCommandBuilderSettings;
import org.example.Common.User;
import org.example.UserInterfaces.cli.io.*;
import org.example.UserInterfaces.MainCycleController;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        Inputer inputer = new ConsoleInputer();
        Parser parser = new StandardParser();
        Outputer outputer = new ConsoleOutputer();
        HeliosBdManager bdManager = new HeliosBdManager();
        Communicator communicator = new HeliosCommunicator("helios.cs.ifmo.ru", 5432);
        User user = new User();
        StandardCommandBuilderSettings settings = new StandardCommandBuilderSettings();
        settings.inputMode = "default";
        MainCycleController mainCycleController = new MainCycleController(inputer, parser, outputer, bdManager, communicator, user, settings);
        mainCycleController.mainCycle();
    }
}