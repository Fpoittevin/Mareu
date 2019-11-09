package com.ocr.francois.mareu.model;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

public class Meeting {
    private int id;
    private String subject;
    private MeetingRoom meetingRoom;
    private List<String> participants;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeStop;

    public Meeting(int id, String subject, MeetingRoom meetingRoom, List<String> participants, LocalDate date, LocalTime timeStart, LocalTime timeStop) {
        this.id = id;
        this.subject = subject;
        this.meetingRoom = meetingRoom;
        this.participants = participants;
        this.date = date;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(LocalTime timeStop) {
        this.timeStop = timeStop;
    }
}
