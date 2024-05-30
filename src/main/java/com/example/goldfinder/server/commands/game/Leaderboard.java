package com.example.goldfinder.server.commands.game;

import com.example.goldfinder.server.GameServer;
import com.example.goldfinder.server.LeaderboardManager;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Leaderboard implements GameServerCommand {
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer player, AbstractGame game, InetSocketAddress address, String[] params) {
        return LeaderboardManager.generateLeaderboardsText(LeaderboardManager.LoadLeaderboards(), Integer.parseInt(params[1])) + "END";
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
