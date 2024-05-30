package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.AppClient;
import com.example.goldfinder.client.GameClient;

public class EndClient implements ClientCommand {
    @Override
    public String getName() {
        return "GAME_END";
    }

    @Override
    public String run(GameClient client, String params) {
        System.out.println("Game ended");
        AppClient.getController().restartButtonAction();
        return null;
    }

    @Override
    public String response(GameClient boi, String msg) {
        return null;
    }
}
