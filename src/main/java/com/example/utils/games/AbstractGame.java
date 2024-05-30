package com.example.utils.games;

import com.example.goldfinder.server.DispatcherServer;
import com.example.goldfinder.server.Grid;
import com.example.utils.players.AbstractPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractGame {
    boolean isRunning = false;
    boolean hasEnded = false;
    int maxPlayers;
    List<AbstractPlayer> players;
    Grid grid;

    public AbstractGame(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.grid = new Grid(DispatcherServer.COLUMN_COUNT * maxPlayers, DispatcherServer.ROW_COUNT * maxPlayers, new Random());
        System.out.println("Game size : " + grid.getRowCount() + "x" + grid.getColumnCount());
    }

    public void addPlayer(AbstractPlayer player) {
        if (!isRunning) {
            players.add(player);
            spawnPlayer(player);
            System.out.println("spawned");
            System.out.println("Player added to game " + this + " " + players.size() + "/" + maxPlayers);
            if (players.size() == maxPlayers) {
                isRunning = true;
            }
        }
    }

    public AbstractPlayer getPlayerFromCoordinates(int xPos, int yPos){
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xPos && p.getyPos() == yPos) {
                return p;
            }
        }
        return null;
    }

    public String getSurrounding(int xPos, int yPos) {
        return "up:" + getUp(xPos, yPos) + "down:" + getDown(xPos, yPos) + "left:" + getLeft(xPos, yPos) + "right:" + getRight(xPos, yPos);
    }

    protected boolean isFree(int xPos, int yPos){
        return getPlayerFromCoordinates(xPos, yPos) == null && !grid.hasGold(xPos, yPos);
    }

    public String getUp(int xPos, int yPos){
        if(grid.upWall(xPos, yPos) || yPos == 0) return "WALL ";
        return getObstacles(xPos, yPos, 0, -1);
    }

    public  String getDown(int xPos, int yPos){
        if(grid.downWall(xPos, yPos) || yPos == grid.getRowCount()) return "WALL ";
        return getObstacles(xPos, yPos, 0, 1);
    }

    public  String getLeft(int xPos, int yPos){
        if(grid.leftWall(xPos, yPos) || xPos == 0) return "WALL ";
        return getObstacles(xPos, yPos, -1, 0);
    }

    public  String getRight(int xPos, int yPos){
        if(grid.rightWall(xPos, yPos) || xPos == grid.getColumnCount()) return "WALL ";
        return getObstacles(xPos, yPos, 1, 0);
    }

    public int getGoldCount() {
        return grid.getGoldCount();
    }

    public void movePlayer(AbstractPlayer player, int xPos, int yPos) {
        players.get(players.indexOf(player)).move(xPos, yPos);

    }
    public void removePlayer(AbstractPlayer player) {
        players.remove(player);
    }

    public AbstractPlayer getPlayer(AbstractPlayer player) {
        return players.get(players.indexOf(player));
    }

    public void collectGold(AbstractPlayer player){
        if(grid.hasGold(player.getxPos(), player.getyPos()) && canCollectGold(player)){
            player.collectGold();
            grid.removeGold(player.getxPos(), player.getyPos());
        }
    }

    protected abstract String getObstacles(int xPos, int yPos, int xdir, int ydir);

    protected abstract void spawnPlayer(AbstractPlayer player);

    protected abstract boolean canCollectGold(AbstractPlayer player);

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public String toString() {
        return "Game with " + players.size() + " players";

    }
    public List<AbstractPlayer> getPlayers() {
        System.out.println(players.size() + " players are in game" );
        return players;
    }

    public void setHasEnded(boolean hasEnded) {
        this.hasEnded = hasEnded;
    }


    public boolean hasEnded() {
        return hasEnded;
    }
}
