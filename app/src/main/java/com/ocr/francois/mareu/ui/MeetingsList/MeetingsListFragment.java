package com.ocr.francois.mareu.ui.MeetingsList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.di.DI;
import com.ocr.francois.mareu.event.NewMeetingEvent;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.service.MeetingApiService;
import com.ocr.francois.mareu.service.MeetingsSorter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MeetingsListFragment extends Fragment implements MeetingsListRecyclerViewAdapter.FragmentCallback {

    public MeetingsSorter.SortParam sortParam;
    @BindView(R.id.fragment_meetings_list_recycler_view)
    RecyclerView recyclerView;
    private List<Meeting> meetings;
    private MeetingApiService meetingApiService;
    private MeetingsListRecyclerViewAdapter meetingsListRecyclerViewAdapter;

    public MeetingsListFragment() {
    }

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

    public void updateMeetingsList() {
        MeetingsSorter.sortMeetings(meetings, sortParam);
        meetingsListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDelete(Meeting meeting) {
        meetingApiService.deleteMeeting(meeting);
        meetingsListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCreateMeeting(NewMeetingEvent event) {
        updateMeetingsList();
    }
}