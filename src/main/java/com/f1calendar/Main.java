package com.f1calendar;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", "net.fortuna.ical4j.util.MapTimeZoneCache");
        Scheduler scheduler = new Scheduler();
        scheduler.runDailyCheck();
    }
}