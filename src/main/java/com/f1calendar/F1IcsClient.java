package com.f1calendar;
import com.google.api.services.calendar.model.Event;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Summary;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class F1IcsClient {

    private static final String ICS_URL =
            "https://files-f1.motorsportcalendars.com/pl/f1-calendar_p1_p2_p3_qualifying_sprint_gp_alarm-30.ics";

    public List<Event> fetchAllEvents() throws Exception {
        List<Event> events = new ArrayList<>();

        URL url = new URL(ICS_URL);
        try (InputStream in = url.openStream()) {
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(in);

            for (Component component : calendar.getComponents(Component.VEVENT)) {
                if (component instanceof VEvent) {
                    VEvent vEvent = (VEvent) component;

                    Optional<Property> summaryPropertyOpt = vEvent.getProperty(Property.SUMMARY);
                    if (summaryPropertyOpt.isPresent()) {
                        Summary summaryObj = (Summary) summaryPropertyOpt.get();
                        String summary = summaryObj.getValue();

                        if (summary.matches(".*(Wyścig|Kwalifikacje|Sprint|trening).*")) {

                            Optional<Property> dtStartPropertyOpt = vEvent.getProperty(Property.DTSTART);
                            if (dtStartPropertyOpt.isPresent()) {
                                Property dtStartProp = dtStartPropertyOpt.get();
                                String startTime = dtStartProp.getValue();

                                if (startTime != null && !startTime.isBlank()) {
                                    events.add(EventMapper.createEvent(summary, startTime));
                                }
                            }
                        }
                    }
                }
            }
        }

        return events;
    }
}