package com.f1calendar;

import com.google.api.services.calendar.model.Event;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scheduler {
    private static final String TRACKED_FILE = "saved_races.txt";

    public void runDailyCheck() throws Exception {
        GoogleCalendarService calendarService = new GoogleCalendarService();
        F1IcsClient client = new F1IcsClient();

        Set<String> saved = readSavedEvents();
        List<Event> events = client.fetchAllEvents();

        int added = 0;
        for (Event event : events) {
            String key = event.getSummary() + event.getStart().getDateTime().toStringRfc3339();
            if (!saved.contains(key)) {
                calendarService.addEvent(event);
                saved.add(key);
                added++;
            }
        }
        saveEvents(saved);
        System.out.println("Dodano " + added + " nowych wydarzeń.");
    }

    private Set<String> readSavedEvents() throws Exception {
        File file = new File(TRACKED_FILE);
        if (!file.exists()) return new HashSet<>();
        return new HashSet<>(Files.readAllLines(file.toPath()));
    }

    private void saveEvents(Set<String> saved) throws Exception {
        try (FileWriter writer = new FileWriter(TRACKED_FILE)) {
            for (String s : saved) {
                writer.write(s + "\n");
            }
        }
    }
}
