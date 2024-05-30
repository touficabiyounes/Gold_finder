package com.example.goldfinder.server.commands.game;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.GameActionProcessor;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Direction implements GameServerCommand {
    AbstractPlayer player;
    AbstractGame game;
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer player, AbstractGame game, InetSocketAddress address, String[] params) {
        String direction = GameActionProcessor.processAction(client, server, player, game, address, params);
        this.player = player;
        this.game = game;
        return direction;
    }
    @Override
    public AbstractGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() { return game.getPlayer(player); }
}
