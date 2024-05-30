package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.GameClient;

public class JoinClient implements ClientCommand {
    @Override
    public String getName() {
        return "GAME_JOIN";
    }

    @Override
    public String run(GameClient client, String params) {
        if (client.isPlaying()) return "You are already in a game!";
        client.sendMessage("GAME_JOIN " + params);
        return "";
    }

    @Override
    public String response(GameClient boi, String msg) {
        ClientCommand resp = ClientCommandInterpreter.parseCommand(msg);
        if (resp != null) msg = resp.run(boi, msg);
        return msg;
    }
}
