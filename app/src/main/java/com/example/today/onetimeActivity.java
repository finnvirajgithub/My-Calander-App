package com.example.today;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class onetimeActivity extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onetime);

        next = findViewById(R.id.buttonOnetime);

        //check if application is opened for the first time
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall","");

        if(FirstTime.equals("Yes")){

            //if application was opened for the firsttime..
            Intent intent = new Intent(onetimeActivity.this, loginscreen.class);
            startActivity(intent);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(onetimeActivity.this, loginscreen.class);
                    startActivity(intent);
                }
            });
        }
        else {
            //Else...
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }
    }
}