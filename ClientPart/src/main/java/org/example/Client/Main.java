package org.example.Client;

import org.example.Client.UserInterfaces.cli.StandardCommandBuilderSettings;
import org.example.Client.UserInterfaces.cli.io.*;
import org.example.Client.UserInterfaces.MainCycleController;
import org.example.Common.Bd.HeliosBdManager;
import org.example.Common.User;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {
        Inputer inputer = new ConsoleInputer();
        Parser parser = new StandardParser();
        Outputer outputer = new ConsoleOutputer();
        HeliosCommunicator communicator = new HeliosCommunicator("localhost", 54321);
        User user = new User();
        StandardCommandBuilderSettings settings = new StandardCommandBuilderSettings();
        settings.inputMode = "default";
        MainCycleController mainCycleController = new MainCycleController(inputer, parser, outputer, communicator, user, settings);
        mainCycleController.mainCycle();
    }
}