package com.ocr.francois.mareu.event;

import com.ocr.francois.mareu.di.DI;
import com.ocr.francois.mareu.model.Meeting;

public class NewMeetingEvent {

    public Meeting meeting;

    public NewMeetingEvent(Meeting meeting) {
        DI.getMeetingApiService().addMeeting(meeting);
    }
}
