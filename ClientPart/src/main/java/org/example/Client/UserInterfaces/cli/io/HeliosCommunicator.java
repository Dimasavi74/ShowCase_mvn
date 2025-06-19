package org.example.Client.UserInterfaces.cli.io;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.example.Common.ServerCommands.ServerCommand;

public class HeliosCommunicator implements Communicator {
    private Socket socket;
    private InetAddress host;
    private int port;
    private InputStream is;
    private OutputStream os;


    public HeliosCommunicator(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        is = socket.getInputStream();
        os = socket.getOutputStream();
        this.socket.setSoTimeout(10000);
    }

    public ServerCommand executeCommand(ServerCommand command) {
        try {
            sendCommand(command);
            ServerCommand newCommand = getCommand();
            return newCommand;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCommand(ServerCommand command) throws IOException {
        System.out.println(command);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.flush();
        oos.writeObject(command);
    }

    private ServerCommand getCommand() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        return (ServerCommand) ois.readObject();
    }
}
