package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.GameClient;

public class SurroundingClient implements ClientCommand {
    @Override
    public String getName() {
        return "SURROUNDING";
    }

    @Override
    public String run(GameClient client, String params) {
        if(client.isPlaying()) {
            client.sendMessage("SURROUNDING");
            return "";
        }
        return "Faut jouer enfaite";
    }

    @Override
    public String response(GameClient boi, String msg) {
        return msg;
    }
}
