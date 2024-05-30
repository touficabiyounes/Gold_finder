package com.example.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public abstract class NetworkHandler {
    protected SocketChannel tcpSocket;
    protected DatagramChannel udpSocket;

    public synchronized String receiveMessage(MessagingProtocol protocol){
        try {
            if(protocol == MessagingProtocol.TCP) {
                return receiveTCPMessage(tcpSocket);
            } else {
                return receiveUDPMessage(udpSocket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendMessage(MessagingProtocol protocol, String message) {
        try {
            if(protocol == MessagingProtocol.TCP) {
                sendTCPMessage(tcpSocket, message);
            } else {
                sendUDPMessage(udpSocket, message);
            }
        }
        catch (IOException e) {
            CustomDebugColor.customError("Could not send message : ");
            e.printStackTrace();
        }
    }

    protected synchronized String receiveTCPMessage(SocketChannel client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        int bytesRead = client.read(buffer);

        if (bytesRead > 0) {
            buffer.flip();
            byte[] receivedBytes = new byte[bytesRead];
            buffer.get(receivedBytes);
            return new String(receivedBytes);
        }

        return "";
    }
    protected synchronized void sendTCPMessage(SocketChannel channel, String message) throws IOException {
        ByteBuffer z = ByteBuffer.allocate(message.getBytes().length);
        z.clear();
        z.put(message.getBytes());
        z.flip();
        channel.write(z);
    }

    synchronized void sendUDPMessage(DatagramChannel channel, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(message.getBytes().length);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();

        ByteBuffer duplicate = ByteBuffer.allocate(128);
        duplicate.clear();
        duplicate.put(message.getBytes());
        duplicate.flip();
        System.out.println("Sending UDP message : " + StandardCharsets.UTF_8.decode(duplicate).toString());
        channel.send(buffer, channel.getRemoteAddress());
    }

    String receiveUDPMessage(DatagramChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.clear();
        try {
            channel.receive(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        String receivedBytes = StandardCharsets.UTF_8.decode(buffer).toString();
        return new String(receivedBytes);
    }
}
