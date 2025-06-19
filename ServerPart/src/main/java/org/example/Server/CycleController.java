package org.example.Server;

import java.util.concurrent.ExecutorService;

public class CycleController {
    public static boolean run = false;
    ServerCommunicator communicator;
    ExecutorService executorService;

    public CycleController(ServerCommunicator com, ExecutorService exe) {
        this.communicator = com;
        this.executorService = exe;
    }

    public static void stop() {
        run = false;
    }

    public void mainCycle() {
        run = true;
        while (run) {
            for (Request request: communicator.getRequests()) {
                request.run();
//                executorService.execute(request);
            }
        }
    }
}
