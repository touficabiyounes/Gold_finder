package com.example.goldfinder.server.commands.game;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.games.GFGame;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class StartGame implements GameServerCommand {
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer player, AbstractGame game, InetSocketAddress address, String[] params) {
        StringBuilder builder = new StringBuilder();
        builder.append("GAME_START ");
        System.out.println(game.getPlayers());
        for(AbstractPlayer joinedPlayer : game.getPlayers()){
            builder.append(joinedPlayer.getName()).append(":").append(game.getPlayers().indexOf(joinedPlayer)).append(" ");
        }
        return builder.toString();
    }

    @Override
    public GFGame getGame() {
        return null;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
    }
}
