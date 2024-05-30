package com.example.goldfinder.server.commands.game;

import com.example.goldfinder.server.GameServer;
import com.example.utils.CustomDebugColor;
import com.example.utils.games.AbstractGame;
import com.example.utils.games.CRGame;
import com.example.utils.games.GFGame;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;
import com.example.utils.players.GFPlayer;
import javafx.util.Pair;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.util.Objects;

public class JoinGame implements GameServerCommand {
    AbstractPlayer player;
    AbstractGame game;

    @Override
    public AbstractGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return game.getPlayer(player);
    }

    public String toString() {
        return "Game_Join";
    }

    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer player, AbstractGame game, InetSocketAddress address, String[] params) {
        System.out.println("Game_Join: " + params[1] + " " + params[2] + "\n");
        String playerName = params[1];
        Pair<Short, AbstractGame> availableGame;
        if (Objects.equals(params[2], "COPS_AND_ROBBERS")) {
            player = new CRPlayer(client, playerName, 0, 0);
            availableGame = server.getGames().getAvailable(CRGame.class, -1);
        } else {
            player = new GFPlayer(client, playerName, 0, 0);
            if (!params[2].endsWith("_SOLO") && !params[2].endsWith("_MASSIVE")) {
                availableGame = server.getGames().getAvailable(GFGame.class, -1);
            } else if (params[2].endsWith("_MASSIVE")) {
                availableGame = server.getGames().getAvailable(GFGame.class, 32);
            } else {
                availableGame = server.getGames().getAvailable(GFGame.class, 1);
            }
        }
        player.setAddress(address);

        game = availableGame.getValue();
        game.addPlayer(player);
        player.joinGame(availableGame.getKey(), (short) game.getPlayers().indexOf(player));

        if (player.getScore() != 0) {
            player.setScore(0);
        }
        this.game = game;
        this.player = player;


        if (game.isRunning()) {
            for (AbstractPlayer p : game.getPlayers()) {
                System.out.println("Sending game start to : " + p + "\n");
                server.sendMessage(p.getClient(), new StartGame().run(p.getClient(), server, p, game, (InetSocketAddress) p.getAddress(), new String[]{}), p.getAddress());
            }
        }

        return CustomDebugColor.customPurple("Player " + playerName + " joined game " + availableGame.getKey());
    }


}
