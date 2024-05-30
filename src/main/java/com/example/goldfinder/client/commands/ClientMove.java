package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.GameClient;

public class ClientMove implements ClientCommand {
    @Override
    public String getName() {
        return "MOVE";
    }

    @Override
    public String run(GameClient client, String params) {
        if(!client.isPlaying()) return "";
        switch(params){
            case "UP" -> client.sendMessage("UP");
            case "DOWN"-> client.sendMessage("DOWN");
            case "LEFT"-> client.sendMessage("LEFT");
            case "RIGHT"-> client.sendMessage("RIGHT");
        }
        return "";
    }

    @Override
    public String response(GameClient boi, String msg) {
        return msg;
    }
}
