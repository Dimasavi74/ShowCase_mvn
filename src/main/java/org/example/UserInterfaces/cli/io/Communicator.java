package org.example.UserInterfaces.cli.io;

import org.example.Common.ServerCommandData.ServerCommandData;
import org.example.Common.ServerCommands.ServerCommand;

public interface Communicator {
    public ServerCommandData executeCommand(ServerCommand command);
}
