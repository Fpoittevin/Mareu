package com.ocr.francois.mareu.ui.MeetingsList;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.di.DI;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.service.MeetingApiService;
import com.ocr.francois.mareu.service.MeetingsSorter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MeetingsListFragment extends Fragment implements MeetingsListRecyclerViewAdapter.FragmentCallback {

    @BindView(R.id.fragment_meetings_list_recycler_view)
    RecyclerView recyclerView;

    private List<Meeting> meetings;
    private MeetingApiService meetingApiService;
    private MeetingsListRecyclerViewAdapter meetingsListRecyclerViewAdapter;
    public MeetingsSorter.SortParam sortParam;

    public MeetingsListFragment() { }

    public static MeetingsListFragment newInstance() {
        return new MeetingsListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meetingApiService = DI.getMeetingApiService();
        meetings = meetingApiService.getMeetings();
        sortParam = MeetingsSorter.SortParam.MEETINGROOM;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meetings_list, container, false);
        ButterKnife.bind(this, view);

        configureRecyclerView();

        return view;
    }

    private void configureRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        meetingsListRecyclerViewAdapter = new MeetingsListRecyclerViewAdapter(meetings, getContext(), this);
        recyclerView.setAdapter(meetingsListRecyclerViewAdapter);
    }

    public void generateMeetingsList() {
        meetings = meetingApiService.getMeetings();
    }

    public void sortMeetingsList() {
        MeetingsSorter.sortMeetings(meetings, sortParam);
        updateMeetingsList();
    }

    public void updateMeetingsList() {
        meetingsListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDelete(Meeting meeting) {
        meetingApiService.deleteMeeting(meeting);
        meetingsListRecyclerViewAdapter.notifyDataSetChanged();
    }
}
