package com.ocr.francois.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListFragment;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ocr.francois.mareu.service.MeetingsSorter.SortParam.DATETIMESTART;
import static com.ocr.francois.mareu.service.MeetingsSorter.SortParam.MEETINGROOM;

public class MainActivity extends AppCompatActivity implements MeetingsListRecyclerViewAdapter.ActivityCallback {

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    MeetingsListFragment meetingsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        configureToolBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        meetingsListFragment = MeetingsListFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.activity_main_frame_layout_main, meetingsListFragment).commit();
    }

    @Override
    public void onItemClick(Meeting meeting) {
        Toast.makeText(this, meeting.getSubject(), Toast.LENGTH_LONG).show();
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

        meetingsListFragment.sortMeetingsList();
        return true;
    }
}