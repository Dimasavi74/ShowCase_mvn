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
        try {
            selector.select();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<SelectionKey> keys = selector.selectedKeys();
        Set<Request> requests = new HashSet<>();
        for (var iter = keys.iterator(); iter.hasNext(); ) {
            SelectionKey key = iter.next();
            System.out.println(key);
            iter.remove();
            Request request = new Request(counter++, key);
            requests.add(request);
        }
        return requests;
    }
}
