package com.ocr.francois.mareu.ui.MeetingCreation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingCreationFragment extends Fragment implements TimePickerDialogFragment.TimesSavedListener {

    private Meeting meeting;
    private MeetingApiService meetingApiService;
    private ArrayAdapter meetingRoomArrayAdapter;
    private List<MeetingRoom> freeMeetingRooms;
    private ParticipantsRecyclerViewAdapter participantsRecyclerViewAdapter;

    @BindView(R.id.fragment_meeting_creation_subject_edit_text)
    TextInputEditText subjectEditText;
    @BindView(R.id.fragment_meeting_creation_date_button)
    MaterialButton dateButton;
    @BindView(R.id.fragment_meeting_creation_times_button)
    MaterialButton timesButton;
    @BindView(R.id.fragment_meeting_creation_meeting_room_spinner)
    Spinner meetingRoomSpinner;
    @BindView(R.id.fragment_meeting_creation_add_participant_edit_text)
    TextInputEditText addParticipantEditText;
    @BindView(R.id.fragment_meeting_creation_add_participant_button)
    MaterialButton addParticipantButton;
    @BindView(R.id.fragment_meeting_creation_participants_recycler_view)
    RecyclerView participantsRecyclerView;

    public static MeetingCreationFragment newInstance() {
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
        displayDateAndTime();

        configureSubjectEditText();
        configureDateButton();
        configureTimesButton();
        configureMeetingRoomSpinner();
        configureParticipantsRecyclerView();
        configureAddParticipantEditText();

        return view;
    }

    private void displayDateAndTime() {
        dateButton.setText(meeting.getDate().toString(getString(R.string.date_pattern)));
        timesButton.setText(meeting.getTimeStart().toString(getString(R.string.time_pattern) + " - " + meeting.getTimeStop().toString(getString(R.string.time_pattern))));
    }

    private void configureSubjectEditText() {
        subjectEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable subject) {
                if(subject.toString().length() == 0) {
                    subjectEditText.setError("veuillez indiquer le sujet de la réunion");
                } else {
                    meeting.setSubject(subject.toString());
                }
            }
        });
    }

    private void configureTimesButton() {
        final TimePickerDialogFragment timesPicker = new TimePickerDialogFragment(meeting.getTimeStart(), meeting.getTimeStop(), this);

        timesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timesPicker.show(getFragmentManager(), "timePicker");
            }
        });
    }

    @Override
    public void onTimesSaved(LocalTime timeStart, LocalTime timeStop) {
        meeting.setTimeStart(timeStart);
        meeting.setTimeStop(timeStop);
        displayDateAndTime();
    }

    private void configureDateButton() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(meeting.getDate().toDate());

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //meeting.setDate(new LocalDate(year, month, day));
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,day);
                meeting.setDate(new LocalDate(cal.getTime()));
                displayDateAndTime();
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                addParticipantButton.setEnabled(Patterns.EMAIL_ADDRESS.matcher(s).matches());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
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


    public void saveMeeting() {
        Boolean error = false;
        if(meeting.getSubject().equals("")){
            error = true;
            subjectEditText.setError("Veuillez indiquer le sujet de la réunion");
        }
        if(meeting.getParticipants().isEmpty()) {
            error = true;
            addParticipantEditText.setError("Veuillez indiquer au moins un participant");
        }

        if(!error) {
            EventBus.getDefault().post(new NewMeetingEvent(meeting));
            getActivity().finish();
        }
    }
}
