package org.example.Server;

import org.example.Common.Bd.HeliosBdManager;
import org.example.Common.ServerCommands.ServerCommand;

import java.io.*;
import java.nio.ByteBuffer;
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
            var data = (ClientData) key.attachment();
            data.isError = true;
            data.errorMessage = "Ошибка выполнения запроса" + exception.getMessage();
            key.attach(data);
            data.command.setError(exception.getMessage());
        }
        System.out.printf("Обработан запрос #%d в потоке id=%d\n", reqNumber, Thread.currentThread().getId());
    }

    public void doAccept() throws IOException {
        var ssc = (ServerSocketChannel) key.channel();
        var sc = ssc.accept();
        key.attach(new ClientData());
        sc.configureBlocking(false);
        sc.register(key.selector(), OP_READ);
    }

    public void doRead() throws IOException {
        var sc = (SocketChannel) key.channel();
        var data = (ClientData) key.attachment();
        sc.read(data.buffer);
        sc.register(key.selector(), OP_WRITE);

        try (ByteArrayInputStream bis = new ByteArrayInputStream(data.buffer.array());
             ObjectInputStream os = new ObjectInputStream(bis)) {
             ServerCommand command = (ServerCommand) os.readObject();
             data.command = command;
             command.setBdManager(new HeliosBdManager());
             command.execute();
        } catch (ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void doWrite() throws IOException {
        var sc = (SocketChannel) key.channel();
        var data = (ClientData) key.attachment();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(bos)) {
            os.writeObject(data.command);
            ByteBuffer outputBuffer = ByteBuffer.allocate(10000);
            outputBuffer.put(bos.toByteArray());
            sc.write(outputBuffer);
        }

    }
}