package com.example.goldfinder.server.commands.game;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Surrounding implements GameServerCommand {
    AbstractPlayer player;
    AbstractGame game;

    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer player, AbstractGame game, InetSocketAddress address, String[] params) {
        this.player = player;
        this.game = game;
        return this.game.getSurrounding(player.getxPos(),player.getyPos());
    }

    @Override
    public AbstractGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return player;
    }
}
