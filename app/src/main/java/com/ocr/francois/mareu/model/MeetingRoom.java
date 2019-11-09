package com.ocr.francois.mareu.model;

import com.ocr.francois.mareu.R;

public enum MeetingRoom {
    A ("Réunion A", R.color.colorRoomA),
    B ("Réunion B", R.color.colorRoomB),
    C ("Réunion C", R.color.colorRoomC),
    D ("Réunion D", R.color.colorRoomD),
    E ("Réunion E", R.color.colorRoomE),
    F ("Réunion F", R.color.colorRoomF),
    G ("Réunion G", R.color.colorRoomG),
    H ("Réunion H", R.color.colorRoomH),
    I ("Réunion I", R.color.colorRoomI),
    J ("Réunion J", R.color.colorRoomJ);

    private String name;
    private int colorId;

    MeetingRoom(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }

    @Override
    public String toString() {
        return name;
    }
}
