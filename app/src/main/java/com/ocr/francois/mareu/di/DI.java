package com.ocr.francois.mareu.di;

import com.ocr.francois.mareu.service.FakeMeetingApiService;
import com.ocr.francois.mareu.service.MeetingApiService;

public class DI {
    private static MeetingApiService service = new FakeMeetingApiService();

    public static MeetingApiService getMeetingApiService() { return service; }

    public static MeetingApiService getNewInstanceMeetingApiService() {
        return new FakeMeetingApiService();
    }
}