package com.example.today;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {

    Button redirectlogin;
    EditText inputEmail, inputPassword, inputComfirmPassword;
    Button btnSignup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnGoogle;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 20;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        redirectlogin = findViewById(R.id.loginredirect);

        inputEmail = findViewById(R.id.signupemail);
        inputPassword = findViewById(R.id.signuppassword);
        inputComfirmPassword = findViewById(R.id.signupconfirmpassword);
        remember = findViewById(R.id.remembermeinsignup);
        btnSignup = findViewById(R.id.signupbtn);
        btnGoogle = findViewById(R.id.signupwithgoogle);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        FirebaseApp.initializeApp(this);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");

        if (checkbox.equals("true")){
            Intent intent = new Intent(signup.this, Home.class);
            startActivity(intent);
        } else if (checkbox.equals("false")) {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleSignIn();
            }
        });

        redirectlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, loginscreen.class);
                startActivity(intent);
            }
        });
        
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()){

                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(signup.this, "Checked", Toast.LENGTH_SHORT).show();

                } else if (!buttonView.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(signup.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void googleSignIn() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            if (data == null) {
                Toast.makeText(this, "Sign-in intent data is null", Toast.LENGTH_SHORT).show();
                return;
            }

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Try to get the signed-in Google account from the task
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    // Use the ID token from the Google account to authenticate with Firebase
                    firebaseAuth(account.getIdToken());
                } else {
                    Toast.makeText(this, "GoogleSignInAccount is null", Toast.LENGTH_SHORT).show();
                }

            } catch (ApiException e) {
                // If sign-in fails, display a more detailed message
                int statusCode = e.getStatusCode();
                String message = "Sign-in failed: " + e.getMessage();

                // Provide user-friendly messages based on the status code
                switch (statusCode) {
                    case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
                        message = "Sign-in was cancelled by the user.";
                        break;
                    case GoogleSignInStatusCodes.SIGN_IN_FAILED:
                        message = "Sign-in failed. Please try again.";
                        break;
                    case GoogleSignInStatusCodes.NETWORK_ERROR:
                        message = "Network error. Please check your connection and try again.";
                        break;
                    case GoogleSignInStatusCodes.INVALID_ACCOUNT:
                        message = "Invalid account. Please check the account and try again.";
                        break;
                    case GoogleSignInStatusCodes.DEVELOPER_ERROR:
                        message = "Developer error. Please check the configuration.";
                        break;
                    default:
                        message = "Unknown error occurred. Please try again.";
                        break;
                }

                Log.e("SignInActivity", "signInResult:failed code=" + statusCode, e);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Handle other possible exceptions
                Log.e("SignInActivity", "signInResult:failed", e);
                Toast.makeText(this, "Sign-in failed with exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            HashMap<String,Object> map = new HashMap<>();
                            map.put("id",user.getUid());
                            map.put("name",user.getDisplayName());
                            map.put("profile",user.getPhotoUrl().toString());

                            database.getReference().child("users").child(user.getUid()).setValue(map);

                            Intent intent = new Intent(signup.this, Home.class);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(signup.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void PerformAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPasword = inputComfirmPassword.getText().toString();

        if (!email.matches(emailPattern)){
            inputEmail.setError("Enter correct Email");
        } else if (password.isEmpty() || password.length()<6) {
            inputPassword.setError("Enter Proper Password");
        } else if (!password.equals(confirmPasword)) {
            inputComfirmPassword.setError("Password not match");
        }else {
            progressDialog.setMessage("Please Wait While Sign Up");
            progressDialog.setTitle("Sign Up");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(signup.this, "Succesfully Sign up", Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(signup.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(signup.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}