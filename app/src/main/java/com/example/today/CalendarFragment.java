package com.example.today;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private List<event_item> eventList;
    private RecyclerView calendarRecylerView;
    private eventAdapter eventAdapter;
    private CalendarView calenderView;
    private DatabaseReference databaseReference;

    public CalendarFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        FloatingActionButton addPage = rootView.findViewById(R.id.addbutton);

        addPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new AddFragment());
                transaction.addToBackStack(null); // This will add the transaction to the back stack so the user can navigate back
                transaction.commit();
            }
        });

        eventList = new ArrayList<>();
        calenderView = rootView.findViewById(R.id.calendarincalender);
        calendarRecylerView = rootView.findViewById(R.id.recycleView);
        calendarRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventAdapter = new eventAdapter(getContext(), getParentFragmentManager(),eventList);
        calendarRecylerView.setAdapter(eventAdapter);


        Calendar today = Calendar.getInstance();

        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH); // Note: January is 0, December is 11
        int day = today.get(Calendar.DAY_OF_MONTH);

        String dateSelected = year + "-" + (month + 1) + "-" + day;
        fetchEvenetsForDate(dateSelected);
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String dateSelected = year + "-" + (month + 1) + "-" + dayOfMonth;
                fetchEvenetsForDate(dateSelected);
            }
        });
        return rootView;
    }

    private void fetchEvenetsForDate(String date){
        databaseReference = FirebaseDatabase.getInstance().getReference(date);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    eventList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        event_item event = snapshot.getValue(event_item.class);
                        if (event != null){
                            event.setFirebaseKey(snapshot.getKey());
                            eventList.add(event);
                        }
                    }
                    eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}