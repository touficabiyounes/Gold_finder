package com.example.goldfinder.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LeaderboardManager {
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    public static synchronized void addToLeaderboards(TreeMap<Integer, ArrayList<String>> scores, int score, String player) {
        for (int existingScore : scores.keySet()) {
            if (scores.get(existingScore).contains(player)) {
                System.out.println("Removing " + player + " from " + existingScore + " score");
                score += existingScore;
                scores.get(existingScore).remove(player);
                if(scores.get(existingScore).isEmpty()) scores.remove(existingScore);
                break;
            }
        }
        scores.computeIfAbsent(score, k -> new ArrayList<>()).add(player);
    }

    public static synchronized String generateLeaderboardsText(TreeMap<Integer, ArrayList<String>> scores, int maxEntries) {
        StringBuilder leaderboardBuilder = new StringBuilder();
        int entryCount = 0;
        for (int score : scores.keySet()) {
            for (String name : scores.get(score)) {
                entryCount++;
                if(entryCount <= maxEntries){;
                    String cleanScore = name.replaceAll("\\[", "").replaceAll("]", "");
                    if (scores.get(score).size() == score || entryCount == maxEntries) {
                        System.out.println("Breaking at " + score + " score");
                        leaderboardBuilder.append("SCORE:").append(score).append(":").append(cleanScore).append("\n");
                        break;
                    }
                    leaderboardBuilder.append("SCORE:").append(score).append(":").append(cleanScore).append("\n");
                }
            }
        }
        return leaderboardBuilder.toString();
    }

    public static synchronized TreeMap<Integer, ArrayList<String>> LoadLeaderboards() {
        TreeMap<Integer, ArrayList<String>> scores = new TreeMap<>(Comparator.reverseOrder());
        try {
            File file = new File(LEADERBOARD_FILE);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" : ");
                int key = Integer.parseInt(line[0]);
                String[] values = line[1].substring(1, line[1].length() - 1).split(", ");
                for (String value : values) {
                    addToLeaderboards(scores, key, value);
                }
            }
            scanner.close();
        } catch (IOException e) {
            return new TreeMap<>();
        }
        return scores;
    }

    public static synchronized void SaveLeaderboards(TreeMap<Integer, ArrayList<String>> scores) {
        try {
            FileWriter writer = new FileWriter(LEADERBOARD_FILE);
            Set<Map.Entry<Integer, ArrayList<String>>> entrySet = scores.entrySet();
            for (Map.Entry<Integer, ArrayList<String>> entry : entrySet) {
                if(entry.getValue().isEmpty()) continue;
                String score = entry.getKey() + " : " + entry.getValue() + "\n";
                score.replaceAll("\\[", "");
                writer.write(score);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

