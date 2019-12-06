package com.ocr.francois.mareu.ui.MeetingDetails;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.di.DI;
import com.ocr.francois.mareu.model.Meeting;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingDetailsActivity extends AppCompatActivity {

    @BindView(R.id.activity_meeting_details_toolbar)
    Toolbar toolbar;
    private MeetingDetailsFragment meetingDetailsFragment;
    private Meeting meeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        int meetingId = getIntent().getIntExtra("meetingId", 0);
        meeting = DI.getMeetingApiService().getMeeting(meetingId);
        meetingDetailsFragment = MeetingDetailsFragment.newInstance();

        ButterKnife.bind(this);
        configureToolBar();

        showFragment();
    }

    private void showFragment() {

        Bundle bundle = new Bundle();
        bundle.putInt("meetingId", meeting.getId());

        meetingDetailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_meeting_details_frame, meetingDetailsFragment)
                .commit();
    }

    private void configureToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}