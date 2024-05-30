package com.example.goldfinder.client.Bot;


import com.example.utils.MessagingProtocol;
import com.example.utils.games.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;


public class AppBot {

    ExecutorService executor;

    List<Bot> bots;

    public static void main(String[] args) {
        //AppBot appBot = new AppBot(2, MessagingProtocol.UDP, GameType.COPS_AND_ROBBERS);
        AppBot appBot = new AppBot(2, MessagingProtocol.UDP, GameType.GOLD_FINDER);
        //AppBot appBot = new AppBot(31, MessagingProtocol.TCP, GameType.GOLD_FINDER_MASSIVE);

        appBot.initBot();
    }

    public AppBot(int botCount, MessagingProtocol MessagingProtocol, GameType gameType){
        executor = Executors.newFixedThreadPool(botCount);
        bots = new ArrayList<>();
        for (int i = 0; i < botCount; i++) {
            bots.add(new Bot(MessagingProtocol, gameType));
        }

    }
    private void initBot(){
        for (Bot bot : bots) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            executor.execute(() -> {
                try {
                    bot.startBot();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
