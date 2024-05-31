package com.example.today;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    TextView textViewEmail, textViewName, textViewPhone;
    Button logOut;
    Button edit;
    ImageView imageView;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        textViewEmail = view.findViewById(R.id.userEmail);
        textViewName = view.findViewById(R.id.userName);
        textViewPhone = view.findViewById(R.id.userPhone);
        imageView = view.findViewById(R.id.profile_image);
        logOut = view.findViewById(R.id.logout);
        edit = view.findViewById(R.id.editpro);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(),gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personPhoto = String.valueOf(acct.getPhotoUrl());
            textViewEmail.setText(personEmail);
            textViewName.setText(personName);
            if (personPhoto != null) {
                Uri uri = Uri.parse(personPhoto);
                imageView.setImageURI(uri);
            }
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new UpdateProfileFragment());
                transaction.addToBackStack(null); // This will add the transaction to the back stack so the user can navigate back
                transaction.commit();
            }
        });

        loadUserDetails();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aleartDialog = new AlertDialog.Builder(getActivity());
                aleartDialog.setTitle("Log out")
                        .setMessage("Are you want to log out?")
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
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
                                        })
                                                .show();
                aleartDialog.create();
            }
        });
        return view;
    }
    private void loadUserDetails() {
        SharedPreferences preferences = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String email = preferences.getString("email", "Email not available");
        String image = preferences.getString("image", null);
        String name = preferences.getString("name", "Name not available");
        String phone = preferences.getString("phone", "Phone not available");

        if (image != null) {
            Uri uri = Uri.parse(image);
            imageView.setImageURI(uri);
        }

        textViewEmail.setText(email);
        textViewName.setText(name);
        textViewPhone.setText(phone);
    }
}