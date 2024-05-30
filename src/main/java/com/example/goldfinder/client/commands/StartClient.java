package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.GameClient;

public class StartClient implements ClientCommand {
    @Override
    public String getName() {
        return "GAME_START";
    }

    @Override
    public String run(GameClient client, String params) {
        System.out.println("Game started");
        client.setPlaying(true);
        return "Game started!";
    }

    @Override
    public String response(GameClient boi, String msg) {
        return "";
    }
}
