package com.example.today;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class UpdateFragment extends Fragment {

    private event_item event;
    private EditText updateEventName;
    private EditText updateEventTime;
    private EditText updateEventAmPm;
    private EditText updateEventNote;
    private CalendarView updateEventCalendar;
    private String stringDateSelected;
    private Button updateButton;
    private Button deleteButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update2, container, false);

        updateEventName = view.findViewById(R.id.updateevent);
        updateEventTime = view.findViewById(R.id.updatetime);
        updateEventAmPm = view.findViewById(R.id.updateampm);
        updateEventNote = view.findViewById(R.id.updatenote);
        updateEventCalendar = view.findViewById(R.id.calendarinupdate);
        updateButton = view.findViewById(R.id.updevent);
        deleteButton = view.findViewById(R.id.dltevent);

        if (getArguments() != null) {
            event = (event_item) getArguments().getSerializable("event");

            if (event != null) {
                updateEventName.setText(event.getEventnameincalendar());
                updateEventTime.setText(event.getEventtimeincalendar());
                updateEventAmPm.setText(event.getAm_pmincalendar());
                updateEventNote.setText(event.getEventnoteincalendar());
            }
        }
        updateEventCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event != null) {
                    String newEventName = updateEventName.getText().toString();
                    String newEventTime = updateEventTime.getText().toString();
                    String newEventAmPm = updateEventAmPm.getText().toString();
                    String newEventNote = updateEventNote.getText().toString();

                    if (stringDateSelected == null) {
                        Toast.makeText(getActivity(), "Date not selected", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("eventnameincalendar", newEventName);
                    hashMap.put("eventtimeincalendar", newEventTime);
                    hashMap.put("am_pmincalendar", newEventAmPm);
                    hashMap.put("eventnoteincalendar", newEventNote);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(stringDateSelected);
                    String firebaseKey = event.getFirebaseKey();
                    if (firebaseKey == null) {
                        Toast.makeText(getActivity(), "Failed to update event: Firebase key is null", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    databaseReference.child(firebaseKey).updateChildren(hashMap)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getActivity(), "Event updated", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Failed to update event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, new CalendarFragment());
                    transaction.addToBackStack(null); // This will add the transaction to the back stack so the user can navigate back
                    transaction.commit();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Delete").setMessage("Do you want to delete event?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (stringDateSelected == null) {
                                    Toast.makeText(getActivity(), "Date not selected", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(stringDateSelected);
                                String firebaseKey = event.getFirebaseKey();
                                databaseReference.child(firebaseKey).removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getActivity(), "Event deleted", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getActivity(), "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.container, new CalendarFragment());
                                transaction.addToBackStack(null); // This will add the transaction to the back stack so the user can navigate back
                                transaction.commit();
                            }
                        }).show();
                alertDialog.create();
            }
        });
        return view;
    }
}