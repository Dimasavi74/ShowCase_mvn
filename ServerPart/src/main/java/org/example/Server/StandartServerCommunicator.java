package org.example.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class StandartServerCommunicator implements ServerCommunicator {
    Selector selector;
    ExecutorService executorService;
    Set<SelectionKey> blockedKeys = new HashSet<>();


    Integer counter = 0;

    public StandartServerCommunicator(ExecutorService exe) {
        executorService = exe;
        ServerSocketChannel server = null;
        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            Scanner console = new Scanner(System.in);
            System.out.print("Введите порт: ");
            String line = console.nextLine();
            server.bind(new InetSocketAddress(Integer.parseInt(line)));
            System.out.println("Сервер открыт?" + server.isOpen());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            System.out.println("Канал для регистрации accept закрыт!");
            throw new RuntimeException(e);
        }
    }

    public Set<Request> checkRequests(){
        try {
            selector.select();
        } catch (IOException e) {
            System.out.println("Select не удался!");
            throw new RuntimeException(e);
        }
        Set<SelectionKey> keys = selector.selectedKeys();
        System.out.println("Эти ключи нашел селектор:");
        Set<Request> requests = new HashSet<>();
        for (var iter = keys.iterator(); iter.hasNext(); ) {
            SelectionKey key = iter.next();
            if (blockedKeys.contains(key)) {
                iter.remove();
                continue;
            }
            System.out.println(key + " isAcceptable: " + key.isAcceptable() + " isReadable: " + key.isReadable() + " isWriteable: " + key.isWritable());
            iter.remove();
            Request request = new Request(counter++, key, blockedKeys);
            System.out.println("Создан новый Request " + (counter - 1));
            executorService.execute(request);
            requests.add(request);
            blockedKeys.add(key);
        }
        return requests;
    }
}
