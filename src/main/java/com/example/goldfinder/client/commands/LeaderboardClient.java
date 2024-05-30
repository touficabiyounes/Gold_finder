package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.GameClient;

public class LeaderboardClient implements ClientCommand {
    @Override
    public String getName() {
        return "LEADER";
    }

    @Override
    public String run(GameClient client, String params) {
        if (!client.isPlaying() && !client.isConnected()) return "You are not connected to a server!";
        client.sendMessage("LEADER " + params);
        return "";
    }

    @Override
    public String response(GameClient boi, String msg) {
        StringBuilder sb = new StringBuilder();
        String[] lines = msg.split("\n");
        for (String line : lines) {
            if(line.contains("END")) break;
            String[] split = line.split(":");
            sb.append(split[1]).append(" ").append(split[2]).append("\n");
        }
        return sb.toString();
    }
}
