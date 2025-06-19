package org.example.Client.UserInterfaces.cli.io;


import org.example.Common.ServerCommands.ServerCommand;

public interface Communicator {
    public ServerCommand executeCommand(ServerCommand command);
}
