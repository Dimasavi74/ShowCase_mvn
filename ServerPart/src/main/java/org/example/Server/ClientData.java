package org.example.Server;

import org.example.Common.ServerCommands.ServerCommand;

import java.nio.ByteBuffer;

public class ClientData {
    public ServerCommand command;
    public ByteBuffer bodyBuffer = ByteBuffer.allocate(1024 * 16);
    public ReadState state = ReadState.HEADER;
    boolean isError = false;
    String errorMessage;
}
