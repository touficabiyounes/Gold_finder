package com.example.goldfinder.server.commands.dispatcher;

import com.example.goldfinder.server.GameServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.List;

public interface IDispatcherServerInterpreter {
    String run(SelectableChannel client, SelectionKey key, List<GameServer> gameServers, InetSocketAddress address, String[] params);
}
