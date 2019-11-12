package com.ocr.francois.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListFragment;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MeetingsListRecyclerViewAdapter.FragmentCallback {

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        configureToolBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        MeetingsListFragment meetingsListFragment = MeetingsListFragment.newInstance();
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
}