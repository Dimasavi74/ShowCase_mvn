package org.example.Client;

import org.example.Client.UserInterfaces.cli.StandardCommandBuilderSettings;
import org.example.Client.UserInterfaces.cli.io.*;
import org.example.Client.UserInterfaces.MainCycleController;
import org.example.Common.User;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Inputer inputer = new ConsoleInputer();
        Parser parser = new StandardParser();
        Outputer outputer = new ConsoleOutputer();
        Scanner console = new Scanner(System.in);
        System.out.print("Введите порт: ");
        String line = console.nextLine();
        HeliosCommunicator communicator = new HeliosCommunicator("localhost", Integer.parseInt(line));
        User user = new User();
        StandardCommandBuilderSettings settings = new StandardCommandBuilderSettings();
        settings.inputMode = "default";
        MainCycleController mainCycleController = new MainCycleController(inputer, parser, outputer, communicator, user, settings);
        mainCycleController.mainCycle();
    }
}