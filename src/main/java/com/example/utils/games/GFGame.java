package com.example.utils.games;

import com.example.goldfinder.server.DispatcherServer;
import com.example.utils.players.AbstractPlayer;

public class GFGame extends AbstractGame {
    int maxCells = 0;
    boolean[][] discoveredCells;

    public GFGame() {
        super(DispatcherServer.DEFAULT_PLAYER_COUNT);
    }

    public GFGame(int maxPlayers) {
        super(maxPlayers);
    }

    @Override
    protected void spawnPlayer(AbstractPlayer player) {
        while (true) {
            int xPos = (int) (Math.random() * grid.getColumnCount());
            int yPos = (int) (Math.random() * grid.getRowCount());
            if (isFree(xPos, yPos)) {
                player.move(xPos, yPos);
                setDiscoveredCell(xPos, yPos);
                break;
            }
        }
    }

    @Override
    protected boolean canCollectGold(AbstractPlayer player) {
        return true;
    }

    public void setDiscoveredCell(int xPos, int yPos) {
        if (discoveredCells == null) {
            discoveredCells = new boolean[grid.getColumnCount()][grid.getRowCount()];
        }
        if (!discoveredCells[xPos][yPos]) {
            discoveredCells[xPos][yPos] = true;
            maxCells++;
        }
        if (xPos + 1 < grid.getColumnCount() && !discoveredCells[xPos+1][yPos] && !grid.rightWall(xPos+1, yPos)) {
            discoveredCells[xPos+1][yPos] = true;
            maxCells++;
        }
        if (xPos-1 >= 0 && !discoveredCells[xPos-1][yPos] && !grid.leftWall(xPos, yPos)) {
            discoveredCells[xPos-1][yPos] = true;
            maxCells++;
        }
        if (yPos+1 < grid.getRowCount() && !discoveredCells[xPos][yPos+1] && !grid.downWall(xPos, yPos)) {
            discoveredCells[xPos][yPos+1] = true;
            maxCells++;
        }
        if(yPos-1 >= 0 && !discoveredCells[xPos][yPos-1] && !grid.upWall(xPos, yPos)) {
            discoveredCells[xPos][yPos-1] = true;
            maxCells++;
        }
    }

    public int getMaxCells() {
        return grid.getColumnCount() * grid.getRowCount();
    }

    public int getDiscoveredCells() {
        return maxCells;
    }

    @Override
    public String toString() {
        return "Goldfinder game with " + players.size() + " players";
    }

    protected String getObstacles(int xPos, int yPos, int x, int y) {
        AbstractPlayer p = getPlayerFromCoordinates(xPos + x, yPos + y);
        if (p != null) {
            return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xPos + x, yPos + y)) return "GOLD ";
        return "EMPTY ";
    }
}
