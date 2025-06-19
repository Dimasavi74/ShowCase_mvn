package org.example.Server;

import org.example.Common.Bd.HeliosBdManager;
import org.example.Common.ChannelWrapper;
import org.example.Common.Exceptions.DefaultException;
import org.example.Common.ServerCommands.ServerCommand;
import org.example.Common.ServerCommands.ServerEmptyCommand;

import java.io.*;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.TreeMap;

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
            throw new RuntimeException(exception);
        } catch (InterruptedException e) {
            throw new DefaultException(e.getMessage());
        }
        System.out.printf("Обработан запрос #%d в потоке id=%d\n", reqNumber, Thread.currentThread().getId());
    }

    public void doAccept() throws IOException {
        System.out.println("Выполняется accept запроса " + reqNumber);
        System.out.println("Ключ: " + key);
        var ssc = (ServerSocketChannel) key.channel();
        System.out.println("Канал: " + ssc);
        SocketChannel sc;
        try {
            sc = ssc.accept();
        } catch (IOException e) {
            System.out.println("Подключение не удалось!");
            throw new RuntimeException(e);
        }
        System.out.println("Подключение удалось!");
        System.out.println("Устанавливаем неблокирующмй режим");
        sc.configureBlocking(false);
        SelectionKey clientKey = null;
        try {
            System.out.println("Устанавливаем неблокирующмй режим");
            clientKey = sc.register(key.selector(), OP_READ);
        } catch (ClosedChannelException exception) {
            System.out.println("Канал закрыт, регистрация ключа на чтение невозможна! Канал: " + sc);
            throw new RuntimeException(exception);
        }
        System.out.println("Ключ на чтение зарегистрирован!: " + clientKey);
        clientKey.attach(new ClientData());
    }

    public void doRead() throws IOException, InterruptedException {
        System.out.println("Выполняется чтение запроса " + reqNumber);
        System.out.println("Ключ: " + key);
        SocketChannel sc = (SocketChannel) key.channel();
        System.out.println("Канал: " + sc);
        sc.configureBlocking(false);
        ClientData data = (ClientData) key.attachment();
        System.out.println("Привязаный объект: " + data);
        ChannelWrapper wrapper = new ChannelWrapper(sc);
        try {
            System.out.println("Читаем объект");
            data.command = (ServerCommand) wrapper.readObject();
            System.out.println("Назначаем бд-менеджер");
            data.command.setBdManager(new HeliosBdManager());
            System.out.println("Исполняем команду");
            data.command.execute();
            if (data.command.getError() != "") {
                System.out.println("Команда выполнена с ошибкой: " + data.command.getError());
                data.isError = true;
                data.errorMessage = data.command.getError();
            } else {
                System.out.println("Команда выполнена успешно!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Объект такого класа не найден");
            data.isError = true;
            data.errorMessage = "SerializationError";
        }
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