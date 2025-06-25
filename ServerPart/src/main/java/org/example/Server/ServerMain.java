package org.example.Server;

import org.example.Common.Bd.HeliosBdManager;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Scanner console = new Scanner(System.in);
        System.out.print("Введите пользователя: ");
        String user = console.nextLine();
        System.out.print("Введите пароль: ");
        String password = console.nextLine();
        HeliosBdManager bdManager = new HeliosBdManager(user, password);
        ServerCommunicator communicator = new StandartServerCommunicator(executorService, bdManager);
        CycleController controller = new CycleController(communicator, executorService);
        controller.mainCycle();

    }
}