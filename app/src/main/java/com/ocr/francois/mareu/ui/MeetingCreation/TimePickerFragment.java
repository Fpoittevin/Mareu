package com.ocr.francois.mareu.ui.MeetingCreation;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.event.TimeChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimePickerFragment extends Fragment {
    @BindView(R.id.fragment_time_picker_time_picker)
    TimePicker timePicker;

    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_MOMENT = "moment";

    public Moment moment;
    public LocalTime time;
    public TimeChangeListener timeChangeListener;

    public enum Moment {START, STOP}

    public TimePickerFragment(TimeChangeListener timeChangeListener) {
        this.timeChangeListener = timeChangeListener;
    }

    public interface TimeChangeListener {
        void onTimeChange(LocalTime time, Moment moment);
    }

    public static TimePickerFragment newInstance(LocalTime time, Moment moment, TimeChangeListener timeChangeListener) {
        TimePickerFragment fragment = new TimePickerFragment(timeChangeListener);

        Bundle args = new Bundle();
        args.putInt(KEY_HOUR, time.getHourOfDay());
        args.putInt(KEY_MINUTE, time.getMinuteOfHour());
        args.putSerializable(KEY_MOMENT, moment);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_picker, container, false);
        ButterKnife.bind(this, view);

        timePicker.setIs24HourView(true);

        LocalTime currentTime = new LocalTime();
        int hour = getArguments().getInt(KEY_HOUR, currentTime.getHourOfDay());
        int minute = getArguments().getInt(KEY_MINUTE, currentTime.getMinuteOfHour());
        moment = (Moment) getArguments().getSerializable(KEY_MOMENT);
        time = new LocalTime(hour, minute);

        configureTimePicker();

        return view;
    }

    public void setTimeInTimePicker() {
        timePicker.setCurrentHour(time.getHourOfDay());
        timePicker.setCurrentMinute(time.getMinuteOfHour());
    }

    public void configureTimePicker() {
        setTimeInTimePicker();

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                time = new LocalTime(hour, minute);
                timeChangeListener.onTimeChange(time, moment);
            }
        });
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

    @Subscribe
    public void onTimeChange(TimeChangeEvent timeChangeEvent) {
        switch (moment) {
            case START:
                time = timeChangeEvent.timeStart;
                break;
            case STOP:
                time = timeChangeEvent.timeStop;
                break;
        }
        setTimeInTimePicker();
    }
}