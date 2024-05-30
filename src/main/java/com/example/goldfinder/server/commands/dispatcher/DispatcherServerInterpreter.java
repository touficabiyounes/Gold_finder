package com.example.goldfinder.server.commands.dispatcher;

public class DispatcherServerInterpreter {
    public static IDispatcherServerInterpreter parseCommand(String command) {
        String prefix = command.split(" ")[0];
        if(prefix.toUpperCase().equals("GAME_JOIN")) return new DispatcherJoinGame();
        else {
            System.out.println("Invalid command : " + command);
            return null;
        }
    }
}
