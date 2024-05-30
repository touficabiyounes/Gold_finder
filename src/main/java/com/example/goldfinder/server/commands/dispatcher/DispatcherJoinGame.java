package com.example.goldfinder.server.commands.dispatcher;

import com.example.goldfinder.server.GameServer;
import com.example.utils.MessagingProtocol;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.List;

import static com.example.goldfinder.server.DispatcherServer.GAME_COUNT;

public class DispatcherJoinGame implements IDispatcherServerInterpreter {
    @Override
    public String run(SelectableChannel client, SelectionKey k, List<GameServer> gameServers, InetSocketAddress address, String[] params) {
        int count = 0;
        int nGames = 0;
        for(GameServer gameServer : gameServers){
            count++;
            nGames += gameServer.getGames().getRunningGames();
            if(gameServer.getGames().getRunningGames() < GAME_COUNT){
                System.out.println("Currently running games : " + nGames);
                System.out.println("Server full : " + count + "/" + gameServers.size());
                return "REDIRECT " + gameServer.getAdress(MessagingProtocol.valueOf((String)k.attachment())).getAddress().getHostAddress() +
                        ":" + gameServer.getAdress(MessagingProtocol.valueOf((String)k.attachment())).getPort();
            }
        }
        return "GAME_END";
    }
}
