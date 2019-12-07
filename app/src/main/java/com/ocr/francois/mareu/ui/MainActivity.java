package com.ocr.francois.mareu.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.model.MeetingRoom;
import com.ocr.francois.mareu.ui.MeetingCreation.MeetingCreationActivity;
import com.ocr.francois.mareu.ui.MeetingDetails.MeetingDetailsActivity;
import com.ocr.francois.mareu.ui.MeetingDetails.MeetingDetailsFragment;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListFragment;
import com.ocr.francois.mareu.ui.MeetingsList.MeetingsListRecyclerViewAdapter;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ocr.francois.mareu.service.MeetingsSorter.SortParam.DATETIMESTART;
import static com.ocr.francois.mareu.service.MeetingsSorter.SortParam.MEETINGROOM;

public class MainActivity extends AppCompatActivity implements MeetingsListRecyclerViewAdapter.ActivityCallback {

    public MeetingsListFragment meetingsListFragment;
    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main_creation_fab)
    FloatingActionButton creationFab;
    MeetingDetailsFragment meetingDetailsFragment;
    FragmentManager fragmentManager;

    private DatePickerDialog datePickerDialog;
    private AlertDialog.Builder meetingRoomsFilterDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        configureToolBar();
        configureDialogs();

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

        if (findViewById(R.id.activity_main_details_frame) == null) {
            Intent meetingDetailsIntent = new Intent(MainActivity.this, MeetingDetailsActivity.class);
            meetingDetailsIntent.putExtra("meetingId", meeting.getId());
            startActivity(meetingDetailsIntent);
        } else {
            if (findViewById(R.id.activity_main_selection_layout) != null) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meetings_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_meetings_list_sort_by_dates:
                meetingsListFragment.sortMeetings(DATETIMESTART);
                break;

            case R.id.menu_meetings_list_sort_by_room:
                meetingsListFragment.sortMeetings(MEETINGROOM);
                break;

            case R.id.menu_meetings_list_filter_by_date:
                datePickerDialog.show();
                break;

            case R.id.menu_meetings_list_filter_by_room:
                meetingRoomsFilterDialogBuilder.show();
                break;

            case R.id.menu_meetings_list_all:
                meetingsListFragment.displayAllMeetings();
                break;
        }
        return true;
    }

    private void configureDialogs() {
        LocalDate today = new LocalDate();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate date = new LocalDate(year, month + 1, day);
                meetingsListFragment.displayMeetingsByDate(date);
            }
        }, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, MeetingRoom.values());
        meetingRoomsFilterDialogBuilder = new AlertDialog.Builder(this);
        meetingRoomsFilterDialogBuilder.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MeetingRoom meetingRoom = (MeetingRoom) arrayAdapter.getItem(i);
                meetingsListFragment.displayMeetingsByMeetingRoom(meetingRoom);
                dialogInterface.dismiss();
            }
        });
    }
}