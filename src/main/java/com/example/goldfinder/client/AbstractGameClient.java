package com.example.goldfinder.client;

import com.example.utils.NetworkHandler;
import com.example.utils.MessagingProtocol;
import com.example.utils.games.GameType;
import com.example.utils.CustomDebugColor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public abstract class AbstractGameClient extends NetworkHandler {
    MessagingProtocol mode = MessagingProtocol.TCP;
    GameType gameType = GameType.GOLD_FINDER_SOLO;

    public void connect() {
        CustomDebugColor.customOrange("Connecting to server...");
        try {
            if (mode == MessagingProtocol.TCP) startTCPConnection();
            else if (mode == MessagingProtocol.UDP) startUDPConnection();
        } catch (IOException e) {
            CustomDebugColor.customError("Connection failed. Exiting...");
            clean();
        }
        if (tcpSocket != null || udpSocket != null) CustomDebugColor.customSuccess("Connected to server!");
    }

    private void startTCPConnection() {
        int attempts = 0;
        while (tcpSocket == null && attempts < 10) {
            try {
                tcpSocket = SocketChannel.open(new InetSocketAddress("172.23.130.195", 1234));//147.94.71.247
                tcpSocket.configureBlocking(false);

            } catch (Exception e) {
                attempts++;
                if (attempts == 10) {
                    CustomDebugColor.customError("Connection failed after 10 tries. Exiting...");
                    clean();
                }
            }
        }
    }

    private void startUDPConnection() throws IOException {
        udpSocket = DatagramChannel.open();
        udpSocket.configureBlocking(false);
        udpSocket.connect(new InetSocketAddress("172.23.130.195", 1234));//147.94.71.247
    }

    public void changeConnection(MessagingProtocol mode) {
        this.mode = mode;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
    public abstract String updateSurrounding(int xpos, int ypos) throws IOException, InterruptedException;

    protected void clean() {
        try {
            if (tcpSocket != null) tcpSocket.close();
            if (udpSocket != null) udpSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

