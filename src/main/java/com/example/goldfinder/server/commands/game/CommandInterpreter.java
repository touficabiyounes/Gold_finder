package com.example.goldfinder.server.commands.game;

public class CommandInterpreter {
    public static GameServerCommand interpretCommand(String command) {
        String prefix = extractPrefix(command);
        return tryInterpretCommand(prefix);
    }

    private static String extractPrefix(String command) {
        return command.split(" ")[0];
    }

    private static GameServerCommand tryInterpretCommand(String prefix) {
        return switch (prefix.toUpperCase()) {
            case "GAME_JOIN":
                yield new JoinGame();
            case "UP", "DOWN", "LEFT", "RIGHT":
                yield new Direction();
            case "SURROUNDING":
                yield new Surrounding();
            case "LEADER":
                yield new Leaderboard();
            default:
                System.out.println("ERROR UNKNOWN COMMAND: " + prefix);
                yield null;
        };
    }
}
