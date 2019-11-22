package com.ocr.francois.mareu.event;

import org.joda.time.LocalTime;

public class TimeChangeEvent {
    public LocalTime timeStart;
    public LocalTime timeStop;

    public TimeChangeEvent(LocalTime timeStart, LocalTime timeStop) {
        this.timeStart = timeStart;
        this.timeStop = timeStop;
    }
}
