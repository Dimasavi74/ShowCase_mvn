package org.example.UserInterfaces.cli.io;

import org.example.Common.ServerCommandData.ServerCommandData;
import org.example.Common.ServerCommands.ServerCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class HeliosCommunicator implements Communicator {
    private Socket socket;
    InetAddress host;
    int port;


    public HeliosCommunicator(InetAddress host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.socket.setSoTimeout(10000);
    }

    public ServerCommandData executeCommand(ServerCommand command) {
        try {
            sendCommand(command);
            ServerCommand newCommand = getCommand();
            return newCommand.getData();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCommand(ServerCommand command) throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(this.socket.getOutputStream());
        os.writeObject(command);
    }

    private ServerCommand getCommand() throws IOException, ClassNotFoundException {
        ObjectInputStream os = new ObjectInputStream(this.socket.getInputStream());
        return (ServerCommand) os.readObject();
    }
}
