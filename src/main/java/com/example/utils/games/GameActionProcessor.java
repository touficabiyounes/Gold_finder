package com.example.utils.games;

import com.example.goldfinder.server.GameServer;
import com.example.goldfinder.server.commands.game.EndGame;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;
import com.example.utils.players.GFPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class GameActionProcessor {
    public static String processAction(SelectableChannel client, GameServer server, AbstractPlayer player, AbstractGame game, InetSocketAddress adress, String[] params) {
        String dir = "INVALID_MOVE";
        switch (params[0].toUpperCase()) {
            case "LEFT" -> {
                dir = game.getLeft(player.getxPos(), player.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || player instanceof CRPlayer) {
                        if (((CRPlayer) player).isDead()) {
                            return "INVALID_MOVE";
                        }
                        if (dir.contains("ENEMY")) {
                            assert game instanceof CRGame;
                            ((CRGame) game).catchRobber(player, game.getPlayerFromCoordinates(player.getxPos() - 1, player.getyPos()));
                        } else { game.movePlayer(player, -1, 0); }
                        handleCREnd(client, server, player, game, adress, dir);
                    } else if (game instanceof GFGame || player instanceof GFPlayer) {
                        game.movePlayer(player, -1, 0);
                        handleGFEnd(client, server, player, game, adress, dir);
                    }
                }
            }
            case "RIGHT" -> {
                dir = game.getRight(player.getxPos(), player.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || player instanceof CRPlayer) {
                        if (((CRPlayer) player).isDead()) {
                            return "INVALID_MOVE";
                        }
                        if (dir.contains("ENEMY")) {
                            assert game instanceof CRGame;
                            ((CRGame) game).catchRobber(player, game.getPlayerFromCoordinates(player.getxPos() + 1, player.getyPos()));
                        } else { game.movePlayer(player, 1, 0); }
                        handleCREnd(client, server, player, game, adress, dir);

                    } else if (game instanceof GFGame || player instanceof GFPlayer) {
                        game.movePlayer(player, 1, 0);
                        handleGFEnd(client, server, player, game, adress, dir);
                    }
                }
            }
            case "UP" -> {
                dir = game.getUp(player.getxPos(), player.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || player instanceof CRPlayer) {
                        if (((CRPlayer) player).isDead()) {
                            return "INVALID_MOVE";
                        }
                        if (dir.contains("ENEMY")) {
                            assert game instanceof CRGame;
                            ((CRGame) game).catchRobber(player, game.getPlayerFromCoordinates(player.getxPos(), player.getyPos() - 1));
                        } else { game.movePlayer(player, 0, -1); }
                        handleCREnd(client, server, player, game, adress, dir);
                    } else if (game instanceof GFGame || player instanceof GFPlayer) {
                        game.movePlayer(player, 0, -1);
                        handleGFEnd(client, server, player, game, adress, dir);
                    }
                }
            }
            case "DOWN" -> {
                dir = game.getDown(player.getxPos(), player.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || player instanceof CRPlayer) {
                        if (((CRPlayer) player).isDead()) {
                            return "INVALID_MOVE";
                        }
                        if (dir.contains("ENEMY")) {
                            assert game instanceof CRGame;
                            ((CRGame) game).catchRobber(player, game.getPlayerFromCoordinates(player.getxPos(), player.getyPos() + 1));
                        } else { game.movePlayer(player, 0, 1); }
                        handleCREnd(client, server, player, game, adress, dir);

                    } else if (game instanceof GFGame || player instanceof GFPlayer) {
                        game.movePlayer(player, 0, 1);
                        handleGFEnd(client, server, player, game, adress, dir);
                    }
                }
            }
        }

        if (game.hasEnded()) {
            Thread t = new Thread(() -> server.saveScore(server.getGames().saveScores(player.getGameID())));
            t.start();
            return new EndGame().run(client, server, player, game, adress, null);
        } else if (!dir.endsWith("WALL ") && !dir.contains("PLAYER") && !dir.endsWith("ENEMY ") && !dir.endsWith("ALLY ")) {
            //System.out.println("VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", ""));
            return "VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", "");
        }

        return "INVALID_MOVE";
    }

    private static void handleGFEnd(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr, String dir) {
        assert game instanceof GFGame;
        ((GFGame) game).setDiscoveredCell(p.getxPos(), p.getyPos());
        if (dir.contains("GOLD")) {
            game.collectGold(p);
        }
        if (((GFGame) game).getMaxCells() == ((GFGame) game).getDiscoveredCells() && game.getGoldCount() == 0) {
            endGame(client, server, p, game, addr);
        }
    }

    private static void handleCREnd(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr, String dir) {
        assert game instanceof CRGame;


        if (dir.contains("GOLD")) {
            game.collectGold(p);
            if (game.getGoldCount() == 0) {
                endGame(client, server, p, game, addr);
                return;
            }
        }

        for (AbstractPlayer robber : ((CRGame) game).getRobbers().keySet()) {
            if (((CRGame) game).getRobbers().get(robber).equals("CAUGHT") && !((CRPlayer) robber).isDead()) {
                ((CRGame) game).decreaseRobberCount();
                ((CRPlayer) robber).setDead(true);
            }
        }
        if (((CRGame) game).getRobberCount() == 0) {
            endGame(client, server, p, game, addr);
        }
    }

    private static void endGame(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr) {
        for (AbstractPlayer abstractPlayer : game.getPlayers()) {
            server.sendMessage(abstractPlayer.getClient(),
                    new EndGame().run(client, server, p, game, addr, null),
                    abstractPlayer.getAddress());
            game.setHasEnded(true);
        }
        if (game instanceof CRGame) {
            for (AbstractPlayer robber : game.getPlayers()) {
                ((CRGame) game).setNeutral((CRPlayer) robber);
            }
        }
    }
}
