package com.ocr.francois.mareu.ui.MeetingCreation;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.tabs.TabLayout;
import com.ocr.francois.mareu.R;

import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TimePickerDialogFragment extends DialogFragment {

    @BindView(R.id.fragment_time_picker_dialog_time_picker)
    public TimePicker timePicker;
    @BindView(R.id.fragment_time_picker_dialog_tab_layout)
    public TabLayout tabLayout;

    public LocalTime timeStart;
    public LocalTime timeStop;
    private Moment momentSelected;
    TimePickerDialogListener listener;

    public interface TimePickerDialogListener {
        void onTimePickerDialogPositiveClick(LocalTime timeStart, LocalTime timeStop);
    }
    private enum Moment{START, STOP}

    public TimePickerDialogFragment(Fragment parent, LocalTime timeStart, LocalTime timeStop) {
        this.timeStart = timeStart;
        this.timeStop = timeStop;

        if(parent instanceof TimePickerDialogListener) {
            listener = (TimePickerDialogListener) parent;
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onTimePickerDialogPositiveClick(timeStart, timeStop);
            }
        });
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_time_picker_dialog, null);
        builder.setView(view);
        ButterKnife.bind(this, view);

        configureTimePicker();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    momentSelected = Moment.START;
                    timePicker.setCurrentHour(timeStart.getHourOfDay());
                    timePicker.setCurrentMinute(timeStart.getMinuteOfHour());
                }else{
                    momentSelected = Moment.STOP;
                    timePicker.setCurrentHour(timeStop.getHourOfDay());
                    timePicker.setCurrentMinute(timeStop.getMinuteOfHour());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    momentSelected = Moment.START;
                    timePicker.setCurrentHour(timeStart.getHourOfDay());
                    timePicker.setCurrentMinute(timeStart.getMinuteOfHour());
                }else{
                    momentSelected = Moment.STOP;
                    timePicker.setCurrentHour(timeStop.getHourOfDay());
                    timePicker.setCurrentMinute(timeStop.getMinuteOfHour());
                }
            }
        });

        return builder.create();
    }

    private void configureTimePicker() {
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(timeStart.getHourOfDay());
        timePicker.setCurrentMinute(timeStart.getMinuteOfHour());
        momentSelected = Moment.START;

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                LocalTime timeSelected = new LocalTime(hour, minute);
                switch (momentSelected) {
                    case START:
                        timeStart = timeSelected;
                        if(timeStop.isBefore(timeStart)){
                            timeStop = timeStart.plusMinutes(45);
                        }
                        break;
                    case STOP:
                        timeStop = timeSelected;
                        if(timeStart.isAfter(timeStop)){
                            timeStart = timeStop.minusMinutes(45);
                        }
                }
            }
        });
    }
}
