package org.example.Client.UserInterfaces.cli.io;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.example.Common.ChannelWrapper;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.ServerCommands.ServerCommand;

public class HeliosCommunicator implements Communicator {
    private SocketChannel channel;
    private String host;
    private int port;

    public HeliosCommunicator(String host, int port) throws IOException {
        channel = SocketChannel.open();
        this.host = host;
        this.port = port;

    }

    public ServerCommand executeCommand(ServerCommand command) {
        try {
            if (!channel.isConnected()) {
                channel = SocketChannel.open();
                channel.connect(new InetSocketAddress(host, port));
            }
            ChannelWrapper wrapper = new ChannelWrapper(channel);
            wrapper.writeObject(command);
            ServerCommand newCommand = (ServerCommand) wrapper.readObject();
            channel.close();
            if (newCommand.getError() != null && !newCommand.getError().equals("")) {
                throw new DefaultException(newCommand.getError());
            }
            return newCommand;
        } catch (IOException e) {
            throw new DefaultException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new DefaultException(e.getMessage());
        }
    }
}
