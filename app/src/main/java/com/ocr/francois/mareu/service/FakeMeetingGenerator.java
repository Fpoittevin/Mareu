package com.ocr.francois.mareu.service;

import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.model.MeetingRoom;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FakeMeetingGenerator {
    public static List<Meeting> FAKE_MEETINGS = Arrays.asList(
            new Meeting(1, "Reu 1", MeetingRoom.A, Arrays.asList("email1@gmail.com", "email2@gmail.com", "email1@gmail.com", "email2@gmail.com"), new LocalDate(2019, 10, 2), new LocalTime(13, 5), new LocalTime()),
            new Meeting(2, "Reu 2", MeetingRoom.B, Arrays.asList("email3@gmail.com", "email4@gmail.com"), new LocalDate(2019, 10, 1), new LocalTime(), new LocalTime()),
            new Meeting(3, "Reu 3", MeetingRoom.C, Arrays.asList("email5@gmail.com", "email6@gmail.com"), new LocalDate(2019, 11, 13), new LocalTime(13,3), new LocalTime())
    );

    static List<Meeting> generateMeetings() { return new ArrayList<>(FAKE_MEETINGS); }
}
