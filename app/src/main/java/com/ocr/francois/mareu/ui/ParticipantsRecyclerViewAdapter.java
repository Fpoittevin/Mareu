package com.ocr.francois.mareu.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ocr.francois.mareu.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParticipantsRecyclerViewAdapter extends RecyclerView.Adapter<ParticipantsRecyclerViewAdapter.ViewHolder> {

    private List<String> participants;

    public ParticipantsRecyclerViewAdapter(List<String> participants) {
        this.participants = participants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_participant_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.email.setText(participants.get(position));
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fragment_list_participant_item_email_text_view)
        public TextView email;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}