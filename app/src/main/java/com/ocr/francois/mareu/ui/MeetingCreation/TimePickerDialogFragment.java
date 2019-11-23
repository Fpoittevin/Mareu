package com.ocr.francois.mareu.ui.MeetingCreation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.event.TimeChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TimePickerDialogFragment extends DialogFragment implements TimePickerFragment.
        TimeChangeListener {

    @BindView(R.id.fragment_time_picker_dialog_view_pager)
    ViewPager viewPager;
    @BindView(R.id.fragment_time_picker_dialog_tab_layout)
    TabLayout tabLayout;
   /* @BindView(R.id.fragment_time_picker_save_button)
    FloatingActionButton saveButton;*/

    private LocalTime timeStart;
    private LocalTime timeStop;
    private TimesSavedListener timesSavedListener;


    public interface TimesSavedListener {
        void onTimesSaved(LocalTime timeStart, LocalTime timeStop);
    }

    public TimePickerDialogFragment(LocalTime timeStart, LocalTime timeStop, TimesSavedListener timesSavedListener) {
        this.timeStart = timeStart;
        this.timeStop = timeStop;
        this.timesSavedListener = timesSavedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_picker_dialog, null);
        ButterKnife.bind(this, view);
        configureViewPager();

        /*saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timesSavedListener.onTimesSaved(timeStart, timeStop);
                dismiss();
            }
        });*/
        return view;
    }

    private void configureViewPager() {

        viewPager.setAdapter(new TimePickerDialogPagerAdapter(getChildFragmentManager(), timeStart, timeStop, this));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_access_time_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_access_time_black_24dp);
    }

    @Override
    public void onTimeChange(LocalTime time, TimePickerFragment.Moment moment) {
        switch (moment) {
            case START:
                timeStart = time;
                if (timeStart.isAfter(timeStop)) {
                    timeStop = timeStart.plusMinutes(45);
                }
                break;
            case STOP:
                timeStop = time;
                if (timeStop.isBefore(timeStart)) {
                    timeStart = timeStop.minusMinutes(45);
                }
                break;
        }
        EventBus.getDefault().post(new TimeChangeEvent(timeStart, timeStop));
    }
}