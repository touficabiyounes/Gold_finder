package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.GameClient;

public interface ClientCommand {
     String getName();
     String run(GameClient client, String params);
     String response(GameClient client, String msg);
}
