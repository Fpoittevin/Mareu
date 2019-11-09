package com.ocr.francois.mareu.service;

import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.model.MeetingRoom;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeMeetingApiService implements MeetingApiService {
    private List<Meeting> meetings = FakeMeetingGenerator.generateMeetings();

    @Override
    public List<Meeting> getMeetings(){
        return meetings;
    };

    @Override
    public Meeting getMeeting(int id) {
        Meeting meeting = null;

        for (int i = 0; i < meetings.size(); i++) {
            if(meetings.get(i).getId() == id) {
                meeting = meetings.get(i);
                break;
            }
        }

        return meeting;
    }

    @Override
    public void addMeeting(Meeting meeting) {
        int id = meetings.get(meetings.size() - 1).getId() + 1;
        meeting.setId(id);

        meetings.add(meeting);
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    @Override
    public List getFreeMeetingRooms(LocalDate date, LocalTime timeStart, LocalTime timeStop) {
        List<MeetingRoom> freeMeetingRooms = new ArrayList<>(Arrays.asList(MeetingRoom.values()));

        for(int i = 0; i < meetings.size(); i++) {
            Meeting meeting = getMeetings().get(i);

            if(meeting.getDate().equals(date)) {
                if(!meeting.getTimeStop().isBefore(meeting.getTimeStart()) ||
                        !meeting.getTimeStop().equals(meeting.getTimeStart()) ||
                        !meeting.getTimeStart().isAfter(meeting.getTimeStop()) ||
                        !meeting.getTimeStart().equals(meeting.getTimeStop())) {

                    freeMeetingRooms.remove(meeting.getMeetingRoom());
                }
            }
        }

        return freeMeetingRooms;
    }
}
