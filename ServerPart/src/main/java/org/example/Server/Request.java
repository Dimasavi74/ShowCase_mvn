package org.example.Server;

import org.example.Common.Bd.HeliosBdManager;
import org.example.Common.ServerCommands.ServerCommand;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

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
        }
        System.out.printf("Обработан запрос #%d в потоке id=%d\n", reqNumber, Thread.currentThread().getId());
    }

    public void doAccept() throws IOException {
        var ssc = (ServerSocketChannel) key.channel();
        var sc = ssc.accept();
        if (sc == null) {
            return;
        }
        sc.configureBlocking(false);
        SelectionKey clientKey = sc.register(key.selector(), OP_READ);
        clientKey.attach(new ClientData());
        key.selector().wakeup();
    }

    public void doRead() throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ClientData data = (ClientData) key.attachment();

        System.out.println(1);
        int bytesRead = sc.read(data.bodyBuffer);
        System.out.println(2);
        if (bytesRead == -1) throw new EOFException("Connection closed");
        System.out.println(3);
        data.bodyBuffer.flip();
        byte[] objectData = new byte[data.bodyBuffer.remaining()];
        data.bodyBuffer.get(objectData);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            System.out.println(4);
            System.out.println(Arrays.toString(objectData));
            try {
                data.command = (ServerCommand) ois.readObject();
                System.out.println(5);
                data.command.setBdManager(new HeliosBdManager());
                System.out.println(6);
                data.command.execute();
                SelectionKey clientKey = sc.register(key.selector(), OP_WRITE);
                System.out.println(7);
                clientKey.attach(data);
                key.selector().wakeup();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public void doWrite() throws IOException {
        var sc = (SocketChannel) key.channel();
        var data = (ClientData) key.attachment();

        data.bodyBuffer.flip();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(bos)) {
            os.writeObject(data.command);
            ByteBuffer outputBuffer = ByteBuffer.allocate(1024 * 16);
            outputBuffer.put(bos.toByteArray());
            sc.write(outputBuffer);
            key.cancel();
            sc.close();
        }

    }
}