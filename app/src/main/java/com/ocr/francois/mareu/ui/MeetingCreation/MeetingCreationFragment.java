package com.ocr.francois.mareu.ui.MeetingCreation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.di.DI;
import com.ocr.francois.mareu.event.NewMeetingEvent;
import com.ocr.francois.mareu.model.Meeting;
import com.ocr.francois.mareu.model.MeetingRoom;
import com.ocr.francois.mareu.service.MeetingApiService;
import com.ocr.francois.mareu.ui.ParticipantsRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingCreationFragment extends Fragment {

    @BindView(R.id.fragment_meeting_creation_subject_edit_text)
    TextInputEditText subjectEditText;
    @BindView(R.id.fragment_meeting_creation_date_button)
    MaterialButton dateButton;
    @BindView(R.id.fragment_meeting_creation_time_start_button)
    MaterialButton timeStartButton;
    @BindView(R.id.fragment_meeting_creation_time_stop_button)
    MaterialButton timeStopButton;
    @BindView(R.id.fragment_meeting_creation_meeting_room_spinner)
    Spinner meetingRoomSpinner;
    @BindView(R.id.fragment_meeting_creation_add_participant_edit_text)
    TextInputEditText addParticipantEditText;
    @BindView(R.id.fragment_meeting_creation_add_participant_button)
    MaterialButton addParticipantButton;
    @BindView(R.id.fragment_meeting_creation_participants_recycler_view)
    RecyclerView participantsRecyclerView;
    private MyTimePickerDialog timePickerDialog;
    private Meeting meeting;
    private MeetingApiService meetingApiService;
    private ArrayAdapter meetingRoomArrayAdapter;
    private List<MeetingRoom> freeMeetingRooms;
    private ParticipantsRecyclerViewAdapter participantsRecyclerViewAdapter;

    static MeetingCreationFragment newInstance() {
        return new MeetingCreationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meetingApiService = DI.getMeetingApiService();
        meeting = new Meeting("", new ArrayList<String>(), new LocalDate(), new LocalTime(), new LocalTime().plusMinutes(45));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_creation, container, false);
        ButterKnife.bind(this, view);
        dateButton.setText(meeting.getDate().toString(getString(R.string.date_pattern)));
        timeStartButton.setText(meeting.getTimeStart().toString(getString(R.string.time_pattern)));
        timeStopButton.setText(meeting.getTimeStop().toString(getString(R.string.time_pattern)));

        configureSubjectEditText();
        configureDateButton();
        configureTimesButtonsAndTimePicker();
        configureMeetingRoomSpinner();
        configureParticipantsRecyclerView();
        configureAddParticipantEditText();

        return view;
    }

    private void configureSubjectEditText() {
        subjectEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable subject) {
                if (subject.toString().length() == 0) {
                    subjectEditText.setError(getString(R.string.error_subject_text_input));
                } else {
                    meeting.setSubject(subject.toString());
                }
            }
        });
    }

    private void configureTimesButtonsAndTimePicker() {
        timePickerDialog = new MyTimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                switch (timePickerDialog.getMoment()) {
                    case START:
                        meeting.setTimeStart(new LocalTime(hour, minute));
                        if (meeting.getTimeStart().isAfter(meeting.getTimeStop())) {
                            meeting.setTimeStop(meeting.getTimeStart().plusMinutes(45));
                        }
                        break;
                    case STOP:
                        meeting.setTimeStop(new LocalTime(hour, minute));
                        if (meeting.getTimeStop().isBefore(meeting.getTimeStart())) {
                            meeting.setTimeStart(meeting.getTimeStop().minusMinutes(45));
                        }
                        break;
                }
                timeStartButton.setText(meeting.getTimeStart().toString(getString(R.string.time_pattern)));
                timeStopButton.setText(meeting.getTimeStop().toString(getString(R.string.time_pattern)));
            }
        }, meeting.getTimeStart().getHourOfDay(), meeting.getTimeStart().getMinuteOfHour(), true, MyTimePickerDialog.Moment.START);

        timeStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePickerDialog(MyTimePickerDialog.Moment.START);
            }
        });
        timeStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePickerDialog(MyTimePickerDialog.Moment.STOP);
            }
        });
    }

    private void initTimePickerDialog(MyTimePickerDialog.Moment moment) {
        timePickerDialog.setMoment(moment);
        switch (moment) {
            case START:
                timePickerDialog.updateTime(meeting.getTimeStart().getHourOfDay(), meeting.getTimeStart().getMinuteOfHour());
                break;
            case STOP:
                timePickerDialog.updateTime(meeting.getTimeStop().getHourOfDay(), meeting.getTimeStop().getMinuteOfHour());
                break;
        }
        timePickerDialog.show();
    }

    private void configureDateButton() {

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                meeting.setDate(new LocalDate(year, month, day));
                dateButton.setText(meeting.getDate().toString(getString(R.string.date_pattern)));
                getAndDisplayFreeMeetingRooms();
            }
        }, meeting.getDate().getYear(), meeting.getDate().getMonthOfYear() - 1, meeting.getDate().getDayOfMonth());

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.updateDate(meeting.getDate().getYear(), meeting.getDate().getMonthOfYear() - 1, meeting.getDate().getDayOfMonth());
                datePickerDialog.show();
            }
        });
    }

    private void getAndDisplayFreeMeetingRooms() {
        freeMeetingRooms = meetingApiService.getFreeMeetingRooms(meeting.getDate(), meeting.getTimeStart(), meeting.getTimeStop());
        meetingRoomArrayAdapter.clear();
        meetingRoomArrayAdapter.addAll(freeMeetingRooms);
        meetingRoomSpinner.setAdapter(meetingRoomArrayAdapter);
    }

    private void configureMeetingRoomSpinner() {
        meetingRoomArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item);
        getAndDisplayFreeMeetingRooms();
        meetingRoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                meeting.setMeetingRoom(freeMeetingRooms.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                meeting.setMeetingRoom(freeMeetingRooms.get(0));
            }
        });
    }

    private void configureAddParticipantEditText() {
        addParticipantButton.setEnabled(false);
        addParticipantEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                addParticipantButton.setEnabled(Patterns.EMAIL_ADDRESS.matcher(s).matches());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        addParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meeting.getParticipants().add(addParticipantEditText.getText().toString());
                participantsRecyclerViewAdapter.notifyDataSetChanged();
                addParticipantEditText.setText("");
            }
        });
    }

    private void configureParticipantsRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        participantsRecyclerView.setLayoutManager(layoutManager);
        participantsRecyclerViewAdapter = new ParticipantsRecyclerViewAdapter(meeting.getParticipants());
        participantsRecyclerView.setAdapter(participantsRecyclerViewAdapter);
    }

    void saveMeeting() {
        boolean error = false;
        if (meeting.getSubject().equals("")) {
            error = true;
            subjectEditText.setError(getString(R.string.error_subject_text_input));
        }
        if (meeting.getParticipants().isEmpty()) {
            error = true;
            addParticipantEditText.setError(getString(R.string.error_empty_participant_list));
        }

        if (!error) {
            EventBus.getDefault().postSticky(new NewMeetingEvent(meeting));
            getActivity().finish();
        }
    }
}
