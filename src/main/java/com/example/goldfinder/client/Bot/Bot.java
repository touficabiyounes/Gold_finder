package com.example.goldfinder.client.Bot;

import com.example.goldfinder.client.GameClient;
import com.example.goldfinder.client.commands.JoinClient;
import com.example.goldfinder.client.commands.ClientCommand;
import com.example.goldfinder.client.commands.ClientMove;
import com.example.goldfinder.server.DispatcherServer;
import com.example.utils.MessagingProtocol;
import com.example.utils.games.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Bot {
    GameClient client;
    int column, row;
    public static int COLUMN_COUNT = DispatcherServer.COLUMN_COUNT * 2;
    public static int ROW_COUNT = DispatcherServer.ROW_COUNT * 2;

    MessagingProtocol connectionMode;
    GameType gameType;
    String name;


    public Bot(MessagingProtocol connectionMode, GameType gameType){
        client = new GameClient();

        column = COLUMN_COUNT / 2;
        row = ROW_COUNT / 2;

        this.connectionMode = connectionMode;
        this.gameType = gameType;
        this.name = "Bot";

    }

    public void startBot() {
        if (!client.isPlaying()) {
            if (!name.isEmpty()) {
                client.changeConnection(connectionMode);
                client.setGameType(gameType);
                client.connect();
                client.sendCommand(new JoinClient(), name + " " + gameType);
                String r = client.sendCommand(new JoinClient(), name + " " + gameType);
                System.out.println("Response to game_join : " + r);
            }
        }

        System.out.println("waiting for game to start");
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::run, 2, 2, TimeUnit.SECONDS);
    }

    public void run() {
        ClientCommand inc_command = client.updateClient();
        if (inc_command != null) inc_command.run(client, "");

        if (!client.isPlaying()) return;

        ArrayList<String> directions = new ArrayList<>();

        List<String> surrounding = new ArrayList<>(List.of(client.updateSurrounding(row, column).split(" ")));
        surrounding.removeIf(s -> s.contains("WALL"));

        //System.out.println("bot's surroundings: " + surrounding);
        for(String s : surrounding){
            directions.add(s.split(":")[0].toUpperCase());
        }
        int random = (int) (Math.random() * directions.size());

        //System.out.println("random direction: " + directions);
        switch (directions.get(random)) {
            case "UP" -> {
                if ((client.sendCommand(new ClientMove(), "UP")).startsWith("VALID_MOVE")) {
                    row = Math.max(0, row - 1);
                    System.out.println("MOVING UP");
                }
            }
            case "LEFT" -> {
                if ((client.sendCommand(new ClientMove(), "LEFT")).startsWith("VALID_MOVE")){
                    column = Math.max(0, column - 1);
                    System.out.println("MOVING LEFT");
                }
            }
            case "DOWN" -> {
                if ((client.sendCommand(new ClientMove(), "DOWN")).startsWith("VALID_MOVE")){
                    row = Math.min(ROW_COUNT - 1, row + 1);
                    System.out.printf("MOVING DOWN");
                }
            }
            case "RIGHT" -> {
                if ((client.sendCommand(new ClientMove(), "RIGHT")).startsWith("VALID_MOVE")) {
                    column = Math.min(COLUMN_COUNT - 1, column + 1);
                    System.out.println("MOVING RIGHT");
                }
            }
            default -> {
                return;
            }
        }
    }
}

