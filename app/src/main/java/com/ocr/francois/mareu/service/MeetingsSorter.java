package com.ocr.francois.mareu.service;

import com.ocr.francois.mareu.model.Meeting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MeetingsSorter {

    static public void sortMeetings(List<Meeting> meetings, final SortParam sortParam) {

        Collections.sort(meetings, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting meeting1, Meeting meeting2) {

                int comparison = 0;

                switch (sortParam) {
                    case MEETINGROOM:
                        comparison = meeting1.getMeetingRoom().compareTo(meeting2.getMeetingRoom());
                        break;
                    case DATETIMESTART:
                        if (meeting1.getDateTimeStart().isBefore(meeting2.getDateTimeStart())) {
                            comparison = -1;
                        } else if (meeting1.getDateTimeStart().isAfter(meeting2.getDateTimeStart())) {
                            comparison = 1;
                        }
                        break;
                }
                return comparison;
            }
        });
    }

    public enum SortParam {MEETINGROOM, DATETIMESTART}
}