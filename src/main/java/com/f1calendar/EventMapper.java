package com.f1calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    public static Event createEvent(String title, String icalDateTimeStr) {
        DateTimeFormatter ICS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC);

        LocalDateTime localDateTime = LocalDateTime.parse(icalDateTimeStr, ICS_FORMAT);
        ZonedDateTime eventStartUtc = localDateTime.atZone(ZoneId.of("UTC"));


        String colorId;
        if (title.contains("Wyścig")) {
            colorId = "11";
        } else if (title.contains("Kwalifikacje") || title.contains("Sprint")) {
            colorId = "9";
        } else if (title.contains("trening")) {
            colorId = "2";
        } else {
            colorId = null;
        }

        ZonedDateTime eventStartPl = eventStartUtc.withZoneSameInstant(ZoneId.of("Europe/Warsaw"));
        eventStartPl = eventStartPl.minusMinutes(5);
        ZonedDateTime eventEndPl = eventStartPl.plusHours(1);

        Event event = new Event();
        event.setSummary(title);
        if (colorId != null) {
            event.setColorId(colorId);
        }

        DateTime start = new DateTime(eventStartPl.toInstant().toString());
        DateTime end = new DateTime(eventEndPl.toInstant().toString());

        event.setStart(new EventDateTime().setDateTime(start).setTimeZone("Europe/Warsaw"));
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone("Europe/Warsaw"));

        return event;
    }
}