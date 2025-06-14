package org.example.Server;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CycleController {
    public boolean run = false;
    ServerCommunicator communicator;
    ExecutorService executorService;

    public CycleController(ServerCommunicator com, ExecutorService exe) {
        this.communicator = com;
        this.executorService = exe;
    }

    public void mainCycle() {
        this.run = true;
        while (this.run) {
            for (Request request: communicator.getRequests()) {
                executorService.execute(request);
            }
        }
    }
}
