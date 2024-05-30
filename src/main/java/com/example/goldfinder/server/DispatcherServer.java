package com.example.goldfinder.server;

import com.example.goldfinder.server.commands.dispatcher.IDispatcherServerInterpreter;
import com.example.goldfinder.server.commands.dispatcher.DispatcherServerInterpreter;
import com.example.utils.CustomDebugColor;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatcherServer extends AbstractServer {
    final static int DEFAULT_PORT = 1234;
    public static final int SERVER_COUNT = 4;
    public static final int GAME_COUNT = 10;
    public static final int DEFAULT_PLAYER_COUNT = 4;
    public static final int ROW_COUNT = 4;
    public static final int COLUMN_COUNT = 4;


    ExecutorService executor;
    List<GameServer> gameServers;

    public DispatcherServer() throws IOException {
        this(DEFAULT_PORT);
    }

    public DispatcherServer(int port) throws IOException {
        super(port);
        gameServers = new ArrayList<>(SERVER_COUNT);
        executor = Executors.newFixedThreadPool(SERVER_COUNT);
        for (int i = 0; i < SERVER_COUNT; i++) {
            gameServers.add(new GameServer(0, GAME_COUNT));
        }
    }

    public void startServer() throws IOException {
        initGameServers();
        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
                try {
                    handleConnection(key);
                    selectedKeys.remove();
                } catch (IOException e) {
                    key.cancel();
                    key.channel().close();
                    CustomDebugColor.customError("Connection with client closed: " + e.getMessage());
                }
            }
        }
    }

    private void handleConnection(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            handleAccept(key);
        } else if (key.isReadable()) {
            if (key.channel() instanceof SocketChannel) {
                handleTCPRead(key);
            } else {
                handleUDPRead(key);
            }
        }
    }

    @Override
    protected void handleTCPRead(SelectionKey key) throws IOException {
        InetSocketAddress senderAddress = (InetSocketAddress) ((SocketChannel) key.channel()).getRemoteAddress();
        String msg = receiveTCPMessage((SocketChannel) key.channel());
        if(!msg.isEmpty()){
            System.out.println("Received message from " + senderAddress + " : " + msg);
            key.attach("TCP");
            handleCommands(key, msg, senderAddress);
        }
    }

    @Override
    protected void handleUDPRead(SelectionKey key) throws IOException {
        Pair<InetSocketAddress, String> messageandIp = receiveUDPMessage(key);
        String msg = messageandIp.getValue();
        InetSocketAddress senderAddress = messageandIp.getKey();
        key.attach("UDP");
        if (!msg.isEmpty()) {
            handleCommands(key, msg, senderAddress);
        }
    }

    private void initGameServers(){
        for (GameServer gameServer : gameServers) {
            executor.execute(() -> {
                try {
                    gameServer.startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void handleCommands(SelectionKey key, String msg, InetSocketAddress senderAddress) {
        IDispatcherServerInterpreter currentCommand = DispatcherServerInterpreter.parseCommand(msg);
        if (currentCommand != null) {
            String response = currentCommand.run(key.channel(), key, gameServers, senderAddress, msg.split(" "));
            System.out.println("Sending to " + senderAddress + " : " + response);
            sendMessage(key.channel(), response, senderAddress);
        } else {
            CustomDebugColor.customError("Invalid command received: " + msg);
    }}

    public static void main(String[] args) {
        DispatcherServer server;
        try {
            server = new DispatcherServer(DEFAULT_PORT);
            System.out.println("dispatcher server should be listening on port " + DEFAULT_PORT);
            server.startServer();
        } catch (IOException e) {
            System.out.println("Error creating server " + e.getMessage());
        }
    }
}
