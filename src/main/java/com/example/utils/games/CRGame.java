package com.example.utils.games;

import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRGame extends AbstractGame {
    List<AbstractPlayer> cops;
    Map<AbstractPlayer, String> robbers;
    int goldCount;

    int robberCount;

    public CRGame(int maxPlayers) {
        super(maxPlayers);
        this.cops = new ArrayList<>();
        this.robbers = new HashMap<>();
        this.goldCount = getGoldCount();
        this.robberCount = 0;
    }

    protected void spawnPlayer(AbstractPlayer player) {
        while (true) {
            int xPos = (int) (Math.random() * grid.getColumnCount());
            int yPos = (int) (Math.random() * grid.getRowCount());
            if (cops.size() < maxPlayers / 2 ){
                System.out.println("Player set as cop");
                setCop((CRPlayer) player);
            } else {
                setRobber((CRPlayer) player);
                System.out.println("Player set as robber");
            }
            if (isFree(xPos, yPos)) {
                player.move(xPos, yPos);
                break;
            }
        }
    }

    public boolean alreadyRobber(AbstractPlayer player) {
        return robbers.containsKey(player);
    }

    @Override
    protected boolean canCollectGold(AbstractPlayer player) {
        return !((CRPlayer) player).isCop();
    }

    private boolean isCop(int xPos, int yPos) {
        CRPlayer p = (CRPlayer) getPlayerFromCoordinates(xPos, yPos);
        return p != null && p.isCop();
    }



    public void setCop(CRPlayer player) {
        robbers.remove(player);
        player.setCop(true);
        cops.add(player);
    }

    public void setRobber(CRPlayer player) {
        player.setCop(false);
        robbers.put(player, "FREE");
        robberCount++;
    }

    public void setNeutral(CRPlayer player) {
        player.setCop(false);
        player.setDead(false);
        robbers.remove(player);
    }

    public CRPlayer getPlayer(AbstractPlayer player) {
        return (CRPlayer) players.get(players.indexOf(player));
    }

    @Override
    public String toString() {
        return "Cops and Robbers game with " + players.size() + " players";
    }


    public Map<AbstractPlayer, String> getRobbers() {
        return robbers;
    }

    public void catchRobber(AbstractPlayer p1, AbstractPlayer p2) {
        if (((CRPlayer) p1).isCop() && !((CRPlayer) p2).isCop() && !robbers.get(p2).equals("CAUGHT")){
            p1.collectGold();
            robbers.remove(p2);
            robbers.put(p2, "CAUGHT");
        } else if(((CRPlayer) p2).isCop() && !((CRPlayer) p1).isCop() && !robbers.get(p1).equals("CAUGHT")){
            p2.collectGold();
            robbers.remove(p1);
            robbers.put(p1, "CAUGHT");
            System.out.println("p1: " + p1 + " p2: " + p2);
        }
    }

    protected String getObstacles(int xPos, int yPos, int x, int y) {
        CRPlayer p = (CRPlayer) getPlayerFromCoordinates(xPos + x, yPos + y);
        if (p != null) {
            if (p.isCop()) {
                return isCop(xPos, yPos) ? "ALLY " : "ENEMY ";
            } else {
                if (!robbers.get(p).equals("CAUGHT") && !p.isDead()) {
                    return isCop(xPos, yPos) ? "ENEMY " : "ALLY ";
                }
            }
        }
        if (grid.hasGold(xPos + x, yPos + y) && !isCop(xPos, yPos)) {
            return "GOLD ";
        }
        return "EMPTY ";
    }

    public int getRobberCount() {
        return robberCount;
    }

    public void decreaseRobberCount() {
        robberCount--;
    }
}
