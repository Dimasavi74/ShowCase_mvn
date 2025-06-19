package org.example.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashSet;
import java.util.Set;

public class StandartServerCommunicator implements ServerCommunicator {
    Selector selector;


    Integer counter = 0;

    public StandartServerCommunicator() {
        ServerSocketChannel server = null;
        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(54321));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Request> getRequests(){
        var sc = (SocketChannel) key.channel();
        var data = (ClientData) key.attachment();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(bos)) {

            os.writeObject(data.command);
            os.flush(); // Важно!

            byte[] bytes = bos.toByteArray();
            ByteBuffer outputBuffer = ByteBuffer.allocate(bytes.length);
            outputBuffer.put(bytes);
            outputBuffer.flip(); // Переключаем в режим чтения

            // Гарантированная отправка всех данных
            while (outputBuffer.hasRemaining()) {
                sc.write(outputBuffer);
            }

        } finally {
            key.cancel();
            sc.close(); // Закрываем после отправки
        }
}
