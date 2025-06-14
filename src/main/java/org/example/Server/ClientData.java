package org.example.Server;

import org.example.Common.ServerCommands.ServerCommand;

import java.nio.ByteBuffer;

public class ClientData {
    public ServerCommand command;
    public ByteBuffer buffer = ByteBuffer.allocate(10000);
    boolean isError = false;
    String errorMessage;
}
