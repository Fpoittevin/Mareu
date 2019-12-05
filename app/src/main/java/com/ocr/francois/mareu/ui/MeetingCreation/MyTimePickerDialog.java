package com.ocr.francois.mareu.ui.MeetingCreation;

import android.app.TimePickerDialog;
import android.content.Context;

public class MyTimePickerDialog extends TimePickerDialog {

    private Moment moment;

    public enum Moment {START, STOP}

    public MyTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView, Moment moment) {
        super(context, listener, hourOfDay, minute, is24HourView);
        this.moment = moment;
    }

    public Moment getMoment() {
        return moment;
    }

    public void setMoment(Moment moment) {
        this.moment = moment;
    }
}
