package com.example.goldfinder.server.commands.game;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class EndGame implements GameServerCommand {
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress address, String[] params) {
        StringBuilder response = new StringBuilder();
        response.append("GAME_END ");
        for(AbstractPlayer player : game.getPlayers()) {
            response.append(player.getName()).append(":").append(player.getScore()).append(" ");
        }
        response.append("END");
        return response.toString();
    }

    @Override
    public AbstractGame getGame() {
        return null;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
    }
}
