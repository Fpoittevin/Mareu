package com.ocr.francois.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.ui.MeetingCreation.MeetingCreationActivity;
import com.ocr.francois.mareu.ui.MeetingDetails.MeetingDetailsActivity;
import com.ocr.francois.mareu.ui.MeetingDetails.MeetingDetailsFragment;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListFragment;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ocr.francois.mareu.service.MeetingsSorter.SortParam.DATETIMESTART;
import static com.ocr.francois.mareu.service.MeetingsSorter.SortParam.MEETINGROOM;

public class MainActivity extends AppCompatActivity implements MeetingsListRecyclerViewAdapter.ActivityCallback {

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main_creation_fab)
    FloatingActionButton creationFab;

    public MeetingsListFragment meetingsListFragment;
    MeetingDetailsFragment meetingDetailsFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        configureToolBar();

        fragmentManager = getSupportFragmentManager();
        meetingsListFragment = MeetingsListFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.activity_main_frame_layout_main, meetingsListFragment).commit();

        creationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meetingCreationIntent = new Intent(MainActivity.this, MeetingCreationActivity.class);
                startActivity(meetingCreationIntent);
            }
        });
    }

    @Override
    public void onItemClick(Meeting meeting) {


        if(findViewById(R.id.activity_main_details_frame) == null) {
            Intent meetingDetailsIntent = new Intent(MainActivity.this, MeetingDetailsActivity.class);
            meetingDetailsIntent.putExtra("meetingId", meeting.getId());
            startActivity(meetingDetailsIntent);
        } else {
            if(findViewById(R.id.activity_main_selection_layout) != null){
                findViewById(R.id.activity_main_selection_layout).setVisibility(View.GONE);
            }
            meetingDetailsFragment = MeetingDetailsFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putInt("meetingId", meeting.getId());
            meetingDetailsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.activity_main_details_frame, meetingDetailsFragment).commit();
        }
    }

    private void configureToolBar() {
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_meetings_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (meetingsListFragment.sortParam) {
            case MEETINGROOM:
                meetingsListFragment.sortParam = DATETIMESTART;
                break;
            case DATETIMESTART:
                meetingsListFragment.sortParam = MEETINGROOM;
        }

        meetingsListFragment.updateMeetingsList();
        return true;
    }
}