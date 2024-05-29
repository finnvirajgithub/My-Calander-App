package com.example.today;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class eventAdapter extends RecyclerView.Adapter<eventAdapter.eventViewHolder> {

    private List<event_item> eventList;
    private FragmentManager fragmentManager;
    private Context context;

    public eventAdapter(Context context,FragmentManager fragmentManager,List<event_item> eventList) {
        this.context = context;
        this.fragmentManager = fragmentManager;
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFragment fragment = new UpdateFragment();
                //create the data bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("event", eventItem);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
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
        CardView cardView;

        public eventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventnameincalendar = itemView.findViewById(R.id.eventnameincalendar);
            eventnoteincalendar = itemView.findViewById(R.id.eventnoteincalendar);
            eventtimeincalendar = itemView.findViewById(R.id.eventtimeincalendar);
            am_pmincalender = itemView.findViewById(R.id.am_pmincalendar);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
