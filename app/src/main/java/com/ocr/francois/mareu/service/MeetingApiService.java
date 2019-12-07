package com.ocr.francois.mareu.service;

import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.model.MeetingRoom;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

public interface MeetingApiService {
    List<Meeting> getMeetings();

    List<Meeting> getMeetingsByDate(LocalDate date);

    List<Meeting> getMeetingsByMeetingRoom(MeetingRoom meetingRoom);

    Meeting getMeeting(int id);

    void addMeeting(Meeting meeting);

    void deleteMeeting(Meeting meeting);

    List getFreeMeetingRooms(LocalDate date, LocalTime timeStart, LocalTime timeStop);
}