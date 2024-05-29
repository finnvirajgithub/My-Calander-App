package com.example.today;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFragment extends Fragment {

    private CalendarView calendarView;
    private EditText eventName;
    private EditText eventTime;
    private EditText eventNote;
    private EditText ampm;
    private Button savebtn;
    private String stringDateSelected;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        calendarView = view.findViewById(R.id.calendarinadd);
        eventName = view.findViewById(R.id.addevent);
        eventNote = view.findViewById(R.id.addnote);
        eventTime = view.findViewById(R.id.addtime);
        ampm = view.findViewById(R.id.addampm);
        savebtn = view.findViewById(R.id.saveevent);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringDateSelected == null) {
                    Toast.makeText(getActivity(), "Please select a date", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = eventName.getText().toString();
                String note = eventNote.getText().toString();
                String time = eventTime.getText().toString();
                String ampmText = ampm.getText().toString();

                if (name.isEmpty() || note.isEmpty() || time.isEmpty() || ampmText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference = FirebaseDatabase.getInstance().getReference(stringDateSelected);
                String eventId = databaseReference.push().getKey();
                event_item event = new event_item(name, note, time, ampmText,eventId);
                databaseReference.child(eventId).setValue(event)
                        .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Event saved", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to save event", Toast.LENGTH_SHORT).show());

                eventName.setText("");
                eventNote.setText("");
                eventTime.setText("");
                ampm.setText("");
            }
        });

        return view;
    }
}