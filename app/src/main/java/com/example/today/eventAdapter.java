package com.example.today;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class eventAdapter extends RecyclerView.Adapter<eventAdapter.eventViewHolder> {

    private List<event_item> eventList;

    public eventAdapter(List<event_item> eventList) {

        this.eventList = eventList;
    }

    @NonNull
    @Override
    public eventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_design, parent, false);
        return new eventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull eventViewHolder holder, int position) {
        event_item eventItem = eventList.get(position);
        holder.eventnameincalendar.setText(eventItem.getEventnameincalendar());
        holder.eventnoteincalendar.setText(eventItem.getEventnoteincalendar());
        holder.eventtimeincalendar.setText(eventItem.getEventtimeincalendar());
        holder.am_pmincalender.setText(eventItem.getAm_pmincalendar());

    }

    @Override
    public int getItemCount() {

        return eventList.size();
    }

    public static class eventViewHolder extends RecyclerView.ViewHolder{
        TextView eventnameincalendar;
        TextView eventnoteincalendar;
        TextView eventtimeincalendar;
        TextView am_pmincalender;

        public eventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventnameincalendar = itemView.findViewById(R.id.eventnameincalendar);
            eventnoteincalendar = itemView.findViewById(R.id.eventnoteincalendar);
            eventtimeincalendar = itemView.findViewById(R.id.eventtimeincalendar);
            am_pmincalender = itemView.findViewById(R.id.am_pmincalendar);
        }
    }
}
