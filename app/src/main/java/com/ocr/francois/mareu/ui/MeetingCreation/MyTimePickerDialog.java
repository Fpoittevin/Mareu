package com.ocr.francois.mareu.ui.MeetingCreation;

import android.app.TimePickerDialog;
import android.content.Context;

class MyTimePickerDialog extends TimePickerDialog {

    private Moment moment;

    MyTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView, Moment moment) {
        super(context, listener, hourOfDay, minute, is24HourView);
        this.moment = moment;
    }

    Moment getMoment() {
        return moment;
    }

    void setMoment(Moment moment) {
        this.moment = moment;
    }

    public enum Moment {START, STOP}
}