package com.example.goldfinder.client.commands;

public class ClientCommandInterpreter {
    public static ClientCommand parseCommand(String command) {
        if(command.isEmpty()) return null;
        return switch(command.split(" ")[0].toUpperCase()) {
            case "GAME_START" -> new StartClient();
            case "GAME_END", "GAME_FULL" -> new EndClient();
            case "REDIRECT" -> new Redirect();
            default -> {
                System.out.println("Invalid command received : " + command); yield null;}
        };
    }
}
