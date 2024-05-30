package com.example.today;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    TextView textViewEmail, textViewName, textViewPhone;
    Button logOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        textViewEmail = view.findViewById(R.id.userEmail);
        textViewName = view.findViewById(R.id.userName);
        textViewPhone = view.findViewById(R.id.userPhone);
        logOut = view.findViewById(R.id.logout);

        loadUserDetails();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                SharedPreferences preferences = requireActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(requireActivity(), loginscreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return view;
    }
    private void loadUserDetails() {
        SharedPreferences preferences = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String email = preferences.getString("email", "Email not available");
        String name = preferences.getString("name", "Name not available");
        String phone = preferences.getString("phone", "Name not available");

        textViewEmail.setText(email);
        textViewName.setText(name);
        textViewPhone.setText(phone);
    }
}