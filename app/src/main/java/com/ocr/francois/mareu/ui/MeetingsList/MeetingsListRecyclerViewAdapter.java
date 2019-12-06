package com.ocr.francois.mareu.ui.MeetingsList;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ocr.francois.mareu.R;
import com.ocr.francois.mareu.model.Meeting;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingsListRecyclerViewAdapter extends RecyclerView.Adapter<MeetingsListRecyclerViewAdapter.MeetingViewHolder> {

    private List<Meeting> meetings;
    private Context context;
    private FragmentCallback fragment;
    private ActivityCallback activity;

    MeetingsListRecyclerViewAdapter(List<Meeting> meetings, Context context, Fragment fragment) {
        this.meetings = meetings;
        this.context = context;
        if (context instanceof ActivityCallback) {
            this.activity = (ActivityCallback) context;
        }
        if (fragment instanceof FragmentCallback) {
            this.fragment = (FragmentCallback) fragment;
        }

    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_list_meeting_item, parent, false);

        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        final Meeting meeting = meetings.get(position);

        String header = meeting.getMeetingRoom() + " - "
                + meeting.getDate().toString(context.getString(R.string.date_pattern)) + " - "
                + meeting.getTimeStart().toString(context.getString(R.string.time_pattern)) + " - "
                + meeting.getSubject();

        String participantsListInString = "";
        for (int i = 0; i < meeting.getParticipants().size(); i++) {
            participantsListInString = participantsListInString + meeting.getParticipants().get(i);

            if (i != meeting.getParticipants().size() - 1) {
                participantsListInString = participantsListInString + ", ";
            }
        }

        GradientDrawable circle = (GradientDrawable) holder.circleColor.getBackground();
        circle.setColor(context.getResources().getColor(meeting.getMeetingRoom().getColorId()));
        holder.headerTextView.setText(header);
        holder.participantTextView.setText(participantsListInString);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onItemClick(meeting);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.onItemDelete(meeting);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public interface ActivityCallback {
        void onItemClick(Meeting item);
    }

    public interface FragmentCallback {
        void onItemDelete(Meeting item);
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fragment_list_meeting_item_circle_image_view)
        ImageView circleColor;
        @BindView(R.id.fragment_list_meeting_item_header_text_view)
        TextView headerTextView;
        @BindView(R.id.fragment_list_meeting_item_participants_text_view)
        TextView participantTextView;
        @BindView(R.id.fragment_list_meeting_item_delete_button)
        ImageButton deleteButton;

        MeetingViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}