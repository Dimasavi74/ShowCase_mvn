package org.example.UserInterfaces.cli.io;

import org.example.Common.ServerCommands.ServerCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HeliosCommunicator implements Communicator {
    private Socket socket;
    InetAddress host;
    int port;


    public HeliosCommunicator(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.socket.setSoTimeout(10000);
    }

    public ServerCommand executeCommand(ServerCommand command) {
        try {
            ServerSocket serv = new ServerSocket(port);
            socket = serv.accept();
            sendCommand(command);
            ServerCommand newCommand = getCommand();
            return newCommand;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCommand(ServerCommand command) throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(this.socket.getOutputStream());
        os.writeObject(command);
    }

    private ServerCommand getCommand() throws IOException, ClassNotFoundException {
        System.out.print(this.socket.getInputStream());
        ObjectInputStream os = new ObjectInputStream(this.socket.getInputStream());
        return (ServerCommand) os.readObject();
    }
}
