package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.GameClient;

import java.net.InetSocketAddress;

public class Redirect implements ClientCommand {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String run(GameClient client, String params) {
        client.redirect(new InetSocketAddress(params.split(" ")[1].split(":")[0], Integer.parseInt(params.split(" ")[1].split(":")[1])));
        return null;
    }

    @Override
    public String response(GameClient boi, String msg) {
        return null;
    }
}
