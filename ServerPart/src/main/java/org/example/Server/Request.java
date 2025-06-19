package org.example.Server;

import org.example.Common.Bd.HeliosBdManager;
import org.example.Common.ChannelWrapper;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.ServerCommands.ServerCommand;
import org.example.Common.ServerCommands.ServerEmptyCommand;

import java.io.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static java.nio.channels.SelectionKey.*;

public class Request implements Runnable {
    private final int reqNumber;
    private final SelectionKey key;

    public Request(int reqNum, SelectionKey k) {
        this.reqNumber = reqNum;
        this.key = k;
    }

    @Override
    public void run() {
        try {
            if (key.isValid()) {
                if (key.isAcceptable()) {
                    doAccept();
                }
                if (key.isReadable()) {
                    doRead();
                }
                if (key.isWritable()) {
                    doWrite();
                }
            }
        } catch (IOException exception) {
//            var data = (ClientData) key.attachment();
//            data.isError = true;
//            data.errorMessage = "Ошибка выполнения запроса" + exception.getMessage();
//            key.attach(data);
//            data.command.setError(exception.getMessage());
//            throw new RuntimeException(exception);
            throw new DefaultException(exception.getMessage());
        } catch (InterruptedException e) {
            throw new DefaultException(e.getMessage());
        }
        System.out.printf("Обработан запрос #%d в потоке id=%d\n", reqNumber, Thread.currentThread().getId());
    }

    public void doAccept() throws IOException {
        var ssc = (ServerSocketChannel) key.channel();
        var sc = ssc.accept();
        sc.configureBlocking(false);
        SelectionKey clientKey = sc.register(key.selector(), OP_READ);
        clientKey.attach(new ClientData());
    }

    public void doRead() throws IOException, InterruptedException {
        SocketChannel sc = (SocketChannel) key.channel();
        sc.configureBlocking(false);
        ClientData data = (ClientData) key.attachment();
        System.out.println(sc);
        ChannelWrapper wrapper = new ChannelWrapper(sc);
        try {
            data.command = (ServerCommand) wrapper.readObject();
        } catch (ClassNotFoundException e) {
            data.isError = true;
            data.errorMessage = "SerializationError";
        }
        System.out.println(data.command);
        data.command.setBdManager(new HeliosBdManager());
        data.command.execute();
        SelectionKey clientKey = sc.register(key.selector(), OP_WRITE);
        clientKey.attach(data);
    }


    public void doWrite() throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        sc.configureBlocking(false);
        ClientData data = (ClientData) key.attachment();
        ChannelWrapper wrapper = new ChannelWrapper(sc);
        ServerCommand command;
        if (data.isError) {
            command = new ServerEmptyCommand();
            command.setError(data.errorMessage);
        } else {
            command = data.command;
        }
        wrapper.writeObject(command);
        key.cancel();
        sc.close();
        }
}