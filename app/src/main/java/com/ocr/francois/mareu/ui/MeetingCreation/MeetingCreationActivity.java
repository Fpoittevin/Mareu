package com.ocr.francois.mareu.ui.MeetingCreation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ocr.francois.mareu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingCreationActivity extends AppCompatActivity {

    @BindView(R.id.activity_meeting_creation_toolbar)
    Toolbar toolbar;

    private MeetingCreationFragment meetingCreationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_creation);

        ButterKnife.bind(this);
        configureToolBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        meetingCreationFragment = MeetingCreationFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.activity_meeting_creation_frame_layout, meetingCreationFragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_meeting_creation, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        meetingCreationFragment.saveMeeting();
        return true;
    }
}
