package com.example.goldfinder.server.commands.game;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public interface GameServerCommand {
    String run(SelectableChannel client, GameServer server, AbstractPlayer player, AbstractGame game, InetSocketAddress address, String[] params);
    AbstractGame getGame();
    AbstractPlayer getPlayer();
}
