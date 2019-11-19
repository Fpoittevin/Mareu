package com.ocr.francois.mareu;

import com.ocr.francois.mareu.di.DI;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.model.MeetingRoom;
import com.ocr.francois.mareu.service.MeetingApiService;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class MeetingServiceTest {

    private MeetingApiService meetingApiService;

    @Before
    public void setup() {
        meetingApiService = DI.getNewInstanceMeetingApiService();
    }

    @Test
    public void addMeetingWithSuccess() {

        Meeting meeting = generateNewMeeting();

        assertTrue(meetingApiService.getMeetings().contains(meeting));
    }

    @Test
    public void deleteMeetingWithSuccess() {

        Meeting meeting = generateNewMeeting();
        meetingApiService.deleteMeeting(meeting);

        assertFalse(meetingApiService.getMeetings().contains(meeting));
    }

    @Test
    public void getMeetingWithSuccess() {
        Meeting meeting = generateNewMeeting();

        assertEquals(meeting, meetingApiService.getMeeting(meeting.getId()));
    }

    @Test
    public void getFreeMeetingRoomWithSuccess() {
        Meeting meeting = generateNewMeeting();

        assertFalse(meetingApiService.getFreeMeetingRooms(meeting.getDate(),meeting.getTimeStart(),meeting.getTimeStop()).contains(meeting.getMeetingRoom()));
    }

    private Meeting generateNewMeeting() {

        Meeting meeting = new Meeting(
                "the subject",
                MeetingRoom.A,
                new ArrayList<String>(),
                new LocalDate(),
                new LocalTime(),
                new LocalTime().plusMinutes(45)
        );

        meeting.getParticipants().add("email1@mareu.com");
        meeting.getParticipants().add("email2@mareu.com");

        meetingApiService.addMeeting(meeting);

        return meeting;
    }
}
