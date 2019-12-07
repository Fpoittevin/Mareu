package com.ocr.francois.mareu.service;

import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.model.MeetingRoom;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class FakeMeetingGenerator {
    private static List<Meeting> FAKE_MEETINGS = Arrays.asList(
            new Meeting(1, "sujet 1", MeetingRoom.A, Arrays.asList("bob@mareu.com", "jeanne@mareu.com", "vincent@mareu.com"), new LocalDate(2019, 12, 12), new LocalTime(13, 5), new LocalTime(13, 50)),
            new Meeting(2, "sujet 2", MeetingRoom.B, Arrays.asList("etienne@mareu.com", "marine@maru.com"), new LocalDate(2019, 12, 10), new LocalTime(15, 15), new LocalTime(15, 45)),
            new Meeting(3, "sujet 3", MeetingRoom.C, Arrays.asList("caroline@mareu.com", "corrine@mareu.com"), new LocalDate(2019, 12, 10), new LocalTime(15, 0), new LocalTime(15, 40))
    );

    static List<Meeting> generateMeetings() {
        return new ArrayList<>(FAKE_MEETINGS);
    }
}