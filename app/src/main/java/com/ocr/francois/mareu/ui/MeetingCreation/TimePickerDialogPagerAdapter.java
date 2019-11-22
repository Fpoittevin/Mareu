package com.ocr.francois.mareu.ui.MeetingCreation;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.joda.time.LocalTime;

public class TimePickerDialogPagerAdapter extends FragmentPagerAdapter {

    private LocalTime timeStart;
    private LocalTime timeStop;
    public TimePickerFragment.TimeChangeListener timeChangeListener;

    public TimePickerDialogPagerAdapter(FragmentManager fragmentManager, LocalTime timeStart, LocalTime timeStop, TimePickerFragment.TimeChangeListener timeChangeListener) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.timeStart = timeStart;
        this.timeStop = timeStop;
        this.timeChangeListener = timeChangeListener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        LocalTime time = new LocalTime();
        TimePickerFragment.Moment moment = TimePickerFragment.Moment.START;

        switch (position) {
            case 0:
                time = timeStart;
                moment = TimePickerFragment.Moment.START;
                break;
            case 1:
                time = timeStop;
                moment = TimePickerFragment.Moment.STOP;
                break;
        }
        return TimePickerFragment.newInstance(time, moment, timeChangeListener);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "début";
                break;
            case 1:
                title = "fin";
                break;
        }
        return title;
    }
}