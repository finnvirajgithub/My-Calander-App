package com.example.today;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateProfileFragment extends Fragment {

    private static final String PREFS_NAME = "userDetails";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";

    ImageView imageview;
    EditText updateName;
    EditText updatePhone;
    Button save;
    FloatingActionButton button;
    ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        imageview = view.findViewById(R.id.profile_image_update);
        button = view.findViewById(R.id.camera);
        updateName = view.findViewById(R.id.updateusername);
        updatePhone = view.findViewById(R.id.updateuserphone);
        save = view.findViewById(R.id.saveprofile);

        SharedPreferences preferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Load saved data from SharedPreferences
        String savedImageUri = preferences.getString(KEY_IMAGE, null);
        String savedName = preferences.getString(KEY_NAME, null);
        String savedPhone = preferences.getString(KEY_PHONE, null);

        if (savedImageUri != null) {
            Uri uri = Uri.parse(savedImageUri);
            imageview.setImageURI(uri);
        }
        if (savedName != null) {
            updateName.setText(savedName);
        }
        if (savedPhone != null) {
            updatePhone.setText(savedPhone);
        }

        // Register the launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        imageview.setImageURI(uri);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(KEY_IMAGE, uri.toString());
                        editor.apply();
                    }
                }
        );

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UpdateProfileFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent(intent -> {
                            imagePickerLauncher.launch(intent);
                            return null;
                        });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updateName.getText().toString();
                String phone = updatePhone.getText().toString();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(KEY_NAME, name);
                editor.putString(KEY_PHONE, phone);
                editor.apply();
                Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new ProfileFragment());
                transaction.addToBackStack(null); // This will add the transaction to the back stack so the user can navigate back
                transaction.commit();
            }
        });
        return view;
    }
}