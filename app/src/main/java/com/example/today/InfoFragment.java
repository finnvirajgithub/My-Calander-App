package com.example.today;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<String>topiclist = new ArrayList<>();
    private ArrayList<String>detaillist = new ArrayList<>();
    private itemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        recyclerView = view.findViewById(R.id.recycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        topiclist = new ArrayList<>();
        detaillist = new ArrayList<>();

        topiclist.add("Version");
        topiclist.add("Android version");
        topiclist.add("Developer");

        detaillist.add("1.0.0");
        detaillist.add("Up to Android 34");
        detaillist.add("S.V. Rajapakshe");

        adapter = new itemAdapter(topiclist,detaillist,getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }
}