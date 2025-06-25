package org.example.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerCommunicator communicator = new StandartServerCommunicator(executorService);
        CycleController controller = new CycleController(communicator, executorService);
        controller.mainCycle();

    }
}