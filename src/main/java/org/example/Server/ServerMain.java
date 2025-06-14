package org.example.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public static void main(String[] args){
        ServerCommunicator communicator = new StandartServerCommunicator();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CycleController controller = new CycleController(communicator, executorService);
        controller.mainCycle();

    }
}