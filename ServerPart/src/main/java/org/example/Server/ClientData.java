package org.example.Server;

import org.example.Common.ServerCommands.ServerCommand;

import java.nio.ByteBuffer;

public class ClientData {
    public ServerCommand command;
    private final Integer BUFFER_SIZE = 16 * 1024;
    public ByteBuffer bodyBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    public ReadState state = ReadState.HEADER;
    boolean isError = false;
    String errorMessage;
}
