package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Date new_date = new Date(18, 11, 1402);
        Date next_date = new_date.nextDay();

        String fileName = "actors.csv";
        Map<String, Actor> actors = readActorsFromCSV(fileName);

        printActorsInfo(actors);
    }

    private static Map<String, Actor> readActorsFromCSV(String fileName) {
        Map<String, Actor> actors = new HashMap<>();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                parseActorLine(line, actors);
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return actors;
    }

    private static void parseActorLine(String line, Map<String, Actor> actors) {
        String[] data = line.split(",");
        if (data.length < 8) {
            System.err.println("Invalid data format: " + line);
            return;
        }

        String actorName = data[0].trim();
        String movieName = data[1].trim();
        int startDay = Integer.parseInt(data[2].trim());
        int startMonth = Integer.parseInt(data[3].trim());
        int startYear = Integer.parseInt(data[4].trim());
        int endDay = Integer.parseInt(data[5].trim());
        int endMonth = Integer.parseInt(data[6].trim());
        int endYear = Integer.parseInt(data[7].trim());

        Actor actor = actors.getOrDefault(actorName, new Actor(actorName));
        actor.addRole(movieName, new Date(startDay, startMonth, startYear), new Date(endDay, endMonth, endYear));
        actors.put(actorName, actor);
    }

    private static void printActorsInfo(Map<String, Actor> actors) {
        for (Actor actor : actors.values()) {
            System.out.println(actor);
            System.out.println("Total days worked: " + actor.getTotalDaysWorked());
            System.out.println("----------------------");
        }
    }
}
