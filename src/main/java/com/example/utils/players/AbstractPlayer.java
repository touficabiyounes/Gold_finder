package com.example.utils.players;

import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;

public abstract class AbstractPlayer {

    String name;
    SocketAddress address;
    SelectableChannel client;
    int score, xPos, yPos;
    Short gameID = null;
    short id;

    public AbstractPlayer(SelectableChannel client, String name, int xPos, int yPos) {
        this.name = name;
        this.client = client;
        this.xPos = xPos;
        this.yPos = yPos;
        score = 0;
    }

    public void move(int xOffset, int yOffset) {
        this.xPos += xOffset;
        this.yPos += yOffset;
    }

    public void joinGame(Short gameID, short id) {
        this.gameID = gameID;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String playerName) {
        this.name = playerName;
    }

    public void setAddress(SocketAddress address) {
        this.address = address;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public Short getGameID() {
        return gameID;
    }
    public short getPlayerID() {
        return id;
    }
    public SelectableChannel getClient() {
        return client;
    }
    public void collectGold() {
        score++;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    @Override
    public String toString(){
        return xPos + "," + yPos;
    }

}
