package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.GameClient;

public class SendCommand {
    public static String run(ClientCommand command, GameClient boi, String params) {
        return command.run(boi, params);
    }
}
