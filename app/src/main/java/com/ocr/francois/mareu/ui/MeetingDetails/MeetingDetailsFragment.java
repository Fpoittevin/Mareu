package com.ocr.francois.mareu.ui.MeetingDetails;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.di.DI;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.service.MeetingApiService;
import com.ocr.francois.mareu.ui.ParticipantsRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.valueOf;

public class MeetingDetailsFragment extends Fragment {

    private MeetingApiService meetingApiService;
    private Meeting meeting;

    @BindView(R.id.fragment_meeting_details_subject_txt)
    TextView subject;
    @BindView(R.id.fragment_meeting_details_room_txt)
    TextView room;

    @BindView(R.id.fragment_meeting_details_date_text)
    TextView date;
    @BindView(R.id.fragment_meeting_details_time_text)
    TextView time;

    @BindView(R.id.fragment_meeting_details_participants_recycler_view)
    RecyclerView participantRecyclerView;

    public static MeetingDetailsFragment newInstance() {
        return new MeetingDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meetingApiService = DI.getMeetingApiService();
        int meetingId = this.getArguments().getInt("meetingId");
        meeting = meetingApiService.getMeeting(meetingId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meeting_details, container, false);

        ButterKnife.bind(this, view);

        subject.setText(meeting.getSubject());
        room.setText(valueOf(meeting.getMeetingRoom()));
        date.setText(meeting.getDate().toString(getString(R.string.date_pattern)));
        time.setText(meeting.getTimeStart().toString(getString(R.string.time_pattern)) + " - " + meeting.getTimeStop().toString(getString(R.string.time_pattern)));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        participantRecyclerView.setLayoutManager(layoutManager);
        participantRecyclerView.setAdapter(new ParticipantsRecyclerViewAdapter(meeting.getParticipants()));

        return view;
    }
}